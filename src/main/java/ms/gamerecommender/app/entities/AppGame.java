package ms.gamerecommender.app.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ms.gamerecommender.business.value.Game;

import javax.annotation.Nullable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
public class AppGame extends Game {
    @Nullable
    Double metacriticScore;

    @Nullable
    Double price;

    public AppGame(int gameId, String title, Set<String> tags, double positiveReviewPercentage, @Nullable Double metacriticScore, @Nullable Double price) {
        super(gameId, title, tags, positiveReviewPercentage);
        this.metacriticScore = metacriticScore;
        this.price = price;
    }
}
