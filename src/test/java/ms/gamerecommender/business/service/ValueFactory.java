package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@UtilityClass
class ValueFactory {
    Integer GAME_ID = 0;
    Integer USER_ID = 1;

    public Game createGame(Set<String> genres, Set<String> tags) {
        return new Game(GAME_ID++, StringUtils.randomAlphanumeric(10), genres, tags);
    }

    public UserGame createUserGame(int gameId, Set<String> genres, Set<String> tags, double timePlayed, double rating, UserGame.Review review) {
        return new UserGame(gameId, StringUtils.randomAlphanumeric(10), genres, tags, timePlayed, rating, review);
    }

    public UserGame createUserGame(Game game, double timePlayed, double rating, UserGame.Review review) {
        return new UserGame(
                game.getGameId(), StringUtils.randomAlphanumeric(10), game.getGenres(), game.getTags(), timePlayed, rating, review
        );
    }

    public UserProfile createUserProfile(List<UserGame> ownedGames) {
        return new UserProfile(USER_ID++, ownedGames);
    }

    public void resetGameId() {
        GAME_ID = 0;
    }
}
