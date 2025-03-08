package ms.gamerecommender.business.value;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;

import javax.sound.sampled.ReverbType;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class UserGame extends Game {

    private static final double RATING_WEIGHT = 0.4;
    private static final double TIME_PLAYED_WEIGHT = 0.4;
    private static final double REVIEW_WEIGHT = 0.2;
    private static final double MAX_TIME_PLAYED = 500.0;

    double timePlayed;
    double rating;
    Review review;

    public UserGame(double timePlayed, double rating) {
        this.review = Review.NONE;
        this.timePlayed = timePlayed;
        this.rating = rating;
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
