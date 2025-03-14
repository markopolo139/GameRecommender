package ms.gamerecommender.business.service.ai;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;

import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeatureVector {
    static int NUMERICAL_FEATURES = 1;
    static List<String> AVAILABLE_TAGS = List.of(
            "Soulslike", "Action", "RPG", "JRPG", "Strategy", "RTS", "Shooter", "Puzzle", "Single-player", "MMO",
            "Fantasy", "Platformer", "Retro", "Third-person", "First-person", "Multiplayer", "History", "Turn-based",
            "Cards", "Roguelike", "Dungeon crawler", "Music", "Visual Novel", "Story", "Adventure", "Open World"
    );

    public static int VECTOR_SIZE = NUMERICAL_FEATURES + AVAILABLE_TAGS.size();

    double positiveReviewPercentage;
    Set<String> tags;

    @Getter(AccessLevel.PUBLIC)
    double targetScore;

    public FeatureVector(Game game) {
        this.positiveReviewPercentage = game.getPositiveReviewPercentage() / 100.0;
        UserGame userGame = game instanceof UserGame ? (UserGame) game: null;

        if (userGame != null) {
            this.targetScore = userGame.calculateScore();
        } else {

            this.targetScore = 0;
        }

        this.tags = game.getTags();
    }

    public float[] getFeatureArray() {
        float[] tagEncoding = encodeTags(tags);

        float[] output = new float[NUMERICAL_FEATURES + tagEncoding.length];

        output[0] = (float) positiveReviewPercentage;

        System.arraycopy(tagEncoding, 0, output, NUMERICAL_FEATURES, tagEncoding.length);

        return output;
    }

    private float[] encodeTags(Set<String> tags) {
        float[] encoding = new float[AVAILABLE_TAGS.size()];

        for (int i = 0; i < AVAILABLE_TAGS.size(); i++) {
            encoding[i] = tags.contains(AVAILABLE_TAGS.get(i)) ? 1.0f : 0.0f;
        }

        return encoding;
    }
}
