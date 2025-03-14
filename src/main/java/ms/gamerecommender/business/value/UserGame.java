package ms.gamerecommender.business.value;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserGame extends Game {

    private static final double RATING_WEIGHT = 0.4;
    private static final double TIME_PLAYED_WEIGHT = 0.4;
    private static final double REVIEW_WEIGHT = 0.2;
    private static final double MAX_TIME_PLAYED = 500.0;

    double timePlayed;
    double rating;
    Review review;

    public UserGame(int gameId, String title, Set<String> tags, double positivePercentage, double timePlayed, double rating) {
        super(gameId, title, tags, positivePercentage);
        this.timePlayed = timePlayed;
        this.rating = rating;
        this.review = Review.NONE;
    }

    public UserGame(int gameId, String title, Set<String> tags, double positivePercentage, double timePlayed, double rating, Review review) {
        super(gameId, title, tags, positivePercentage);
        this.timePlayed = timePlayed;
        this.rating = rating;
        this.review = review;
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    public enum Review {
        POSITIVE(1.0),
        NONE(0.5),
        NEGATIVE(0.0);

        double impact;
    }

    public double calculateScore() {
        val normalizedRating = rating / 5;
        val normalizedTimePlayed = Math.log(Math.min(MAX_TIME_PLAYED, timePlayed) + 1) / Math.log(MAX_TIME_PLAYED + 1);
        return normalizedRating * RATING_WEIGHT + normalizedTimePlayed * TIME_PLAYED_WEIGHT + review.impact * REVIEW_WEIGHT;
    }
}
