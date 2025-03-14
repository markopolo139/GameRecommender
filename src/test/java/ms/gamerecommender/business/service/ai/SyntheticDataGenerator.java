package ms.gamerecommender.business.service.ai;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SyntheticDataGenerator {
    Random random = new Random(156042);

    List<String> availableTags = List.of(
            "Soulslike", "Action", "RPG", "JRPG", "Strategy", "RTS", "Shooter", "Puzzle", "Single-player", "MMO",
            "Fantasy", "Platformer", "Retro", "Third-person", "First-person", "Multiplayer", "History", "Turn-based",
            "Cards", "Roguelike", "Dungeon crawler", "Music", "Visual Novel", "Story", "Adventure", "Open World"
    );

    public Game createGame(int id, Set<String> tags, double positivePercent) {
        return new Game(id, StringUtils.randomAlphanumeric(10), tags, positivePercent);
    }

    private UserGame createUserGame(Game game, double timePlayed, double rating, UserGame.Review review) {
        return new UserGame(
                game.getGameId(), StringUtils.randomAlphanumeric(10), game.getTags(), game.getPositiveReviewPercentage(), timePlayed, rating, review
        );
    }

    public List<Game> createDataset(int datasetSize, int maxNumberOfTagsPerGame) {
        val dataset = new ArrayList<Game>();
        for(int i = 0; i < datasetSize; i++) {
            val randomTags = new HashSet<String>();

            for (int j = 0; j < maxNumberOfTagsPerGame; j++) {
                randomTags.add(availableTags.get(random.nextInt(availableTags.size())));
            }

            dataset.add(createGame(i, randomTags, Math.round(random.nextDouble() * 50 * 100) / 100.0 + 50));
        }

        return dataset;
    }

    public UserProfile createUserProfile(int userId, List<Game> dataset, int maxNumberOfOwnedGames) {
        val ownedGames = new ArrayList<UserGame>();

        for (int j = 0; j < maxNumberOfOwnedGames; j++) {
            val selectedGame = dataset.get(random.nextInt(dataset.size()));
            val userRating = Math.round(random.nextDouble() * 5 * 100) / 100.0;
            ownedGames.add(
                    createUserGame(
                            selectedGame, Math.round(random.nextDouble() * 500 * 100) / 100.0, userRating,
                            userRating > 3.0 ? UserGame.Review.POSITIVE : userRating < 2.0 ? UserGame.Review.NEGATIVE : UserGame.Review.NONE
                    )
            );
        }

        return new UserProfile(userId, ownedGames);
    }
}
