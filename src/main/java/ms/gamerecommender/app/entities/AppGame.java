package ms.gamerecommender.app.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ms.gamerecommender.business.value.Game;

import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class AppGame extends Game {
    double metacriticScore;
    double price;

    public AppGame(int gameId, String title, Set<String> tags, double positiveReviewPercentage, double metacriticScore, double price) {
        super(gameId, title, tags, positiveReviewPercentage);
        this.metacriticScore = metacriticScore;
        this.price = price;
    }
}
