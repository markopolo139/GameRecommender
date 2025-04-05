package ms.gamerecommender.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.app.entities.AppGame;
import ms.gamerecommender.app.entities.steam.ReviewSteamResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SteamApiService {
    static String STEAM_API_KEY = "5EB98254243F08E58EAF06842129AE72";
    static String STEAM_REVIEWS_URL = "https://store.steampowered.com/appreviews/%s?language=all&json=1";
    static String STEAM_APP_INFO_URL = "https://store.steampowered.com/api/appdetails?appids=";
    static String STEAM_API_BASE_URL = "https://api.steampowered.com/";

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    public AppGame getAppInfo(int appId) {
        String url = STEAM_APP_INFO_URL + appId;
        val appGame = restTemplate.getForObject(url, AppGame.class);

        url = STEAM_REVIEWS_URL.formatted(appId);
        val reviewSteamResponse = restTemplate.getForObject(url, ReviewSteamResponse.class);

        assert appGame != null;
        appGame.setPositiveReviewPercentage(
                100 * (double) reviewSteamResponse.totalPositiveReviews() / (reviewSteamResponse.totalPositiveReviews() + reviewSteamResponse.totalNegativeReviews())
        );

        return appGame;
    }
}
