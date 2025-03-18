package ms.gamerecommender.business.service.ai.dl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ms.gamerecommender.business.service.RecommenderUtils.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeatureVector {
    static int NUMERICAL_FEATURES = 1;
    static List<String> AVAILABLE_TAGS = readTagsFromFile();
    static Map<String, Integer> TAG_INDEX_MAP = IntStream.range(0, AVAILABLE_TAGS.size())
            .boxed()
            .collect(Collectors.toMap(AVAILABLE_TAGS::get, i -> i));

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

        for (String tag: tags) {
            encoding[TAG_INDEX_MAP.get(tag)] = 1.0f;
        }

        return encoding;
    }
}
