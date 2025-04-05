package ms.gamerecommender.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ms.gamerecommender.app.entities.AppGame;
import ms.gamerecommender.app.entities.steam.AppListSteamResponse;
import ms.gamerecommender.app.entities.steam.ReviewSteamResponse;
import ms.gamerecommender.app.entities.steam.UserOwnedGamesResponse;
import ms.gamerecommender.app.exceptions.AppException;
import ms.gamerecommender.app.persistence.UserGameEntity;
import ms.gamerecommender.app.persistence.UserGameId;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.app.persistence.repo.GameRepository;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import ms.gamerecommender.business.value.UserGame;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ms.gamerecommender.app.AppUtils.*;
import static ms.gamerecommender.app.entities.AppConverters.*;

//TODO: it should be just service with api calls to steam and things like creating dataset or updating userOwnedGames should be in their respective services (like Game Service)
//  Create new exception instead of using AppException
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Log4j2
public class SteamApiService {
    static String STEAM_API_KEY = "5EB98254243F08E58EAF06842129AE72";
    static String STEAM_REVIEWS_URL = "https://store.steampowered.com/appreviews/%s?language=all&json=1";
    static String STEAM_APP_INFO_URL = "https://store.steampowered.com/api/appdetails?appids=";
    static String STEAM_API_BASE_URL = "https://api.steampowered.com/";

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    GameRepository gameRepository;
    UserProfileRepository userProfileRepository;

    public AppGame getAppInfo(int appId) {
        String url = STEAM_APP_INFO_URL + appId;
        val appGame = restTemplate.getForObject(url, AppGame.class);

        url = STEAM_REVIEWS_URL.formatted(appId);
        val reviewSteamResponse = restTemplate.getForObject(url, ReviewSteamResponse.class);

        if (appGame == null || reviewSteamResponse == null) {
            log.error("AppGame or ReviewSteamResponse is null (appId: {})", appId);
            throw new AppException("AppGame or ReviewSteamResponse is null");
        }

        appGame.setPositiveReviewPercentage(
                100 * (double) reviewSteamResponse.totalPositiveReviews() / (reviewSteamResponse.totalPositiveReviews() + reviewSteamResponse.totalNegativeReviews())
        );

        return appGame;
    }

    public void setOrUpdateUserOwnedGames(int steamId) {
        val userProfile = getCurrentUserProfile();

        val url =  STEAM_API_BASE_URL + "IPlayerService/GetOwnedGames/v1/?key=" + STEAM_API_KEY + "&steamId=" + steamId + "&include_played_free_games=true";
        val gamesOwnedResponse = restTemplate.getForObject(url, UserOwnedGamesResponse.class);

        if (gamesOwnedResponse == null) {
            log.info("GamesOwnedGamesResponse is null (steamId: {})", steamId);
            throw new AppException("GamesOwnedGamesResponse is null");
        }

        userProfile.setUserGames(updateUserGamesInfo(
                userProfile.getUserGames(),
                gamesOwnedResponse.userOwnedGamesResponse().stream().map(
                        it -> new UserGameEntity(userProfile, gameRepository.getReferenceById(it.appid()), it.playedTime(), 0, UserGame.Review.NONE, 0)
                ).collect(Collectors.toSet())
        ));
        userProfileRepository.save(userProfile);
    }

    public void completeDatabase() {
        val url = STEAM_API_BASE_URL + "ISteamApps/GetAppList/v2";
        val appListResponse = restTemplate.getForObject(url, AppListSteamResponse.class);

        if (appListResponse == null) {
            log.error("AppListSteamResponse is null");
            throw new AppException("AppListSteamResponse is null");
        }

        appListResponse.appIds().forEach(it -> {
            val appGame = getAppInfo(it);
            gameRepository.save(convertToGameEntity(appGame));
        });
    }

    private Set<UserGameEntity> updateUserGamesInfo(Set<UserGameEntity> userGameEntities, Set<UserGameEntity> newUserGameEntities) {
        HashSet<UserGameEntity> updatedUserGames = new HashSet<>();

        Map<UserGameId, UserGameEntity> oldGamesMap = userGameEntities.stream()
                .collect(Collectors.toMap(UserGameEntity::getUserGameId, game -> game));

        newUserGameEntities.forEach(newGame -> {
            UserGameEntity updatedGame = oldGamesMap.get(newGame.getUserGameId());
            if (updatedGame != null) {
                updatedGame.setTimePlayed(newGame.getTimePlayed());
            }
            else {
                updatedGame = newGame;
            }

            updatedUserGames.add(updatedGame);
        });

        return updatedUserGames;
    }

    private UserProfileEntity getCurrentUserProfile() {
        return userProfileRepository.findById(getUserID()).orElseThrow();
    }
}
