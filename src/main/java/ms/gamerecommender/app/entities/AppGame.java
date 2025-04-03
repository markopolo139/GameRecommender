package ms.gamerecommender.app.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.app.entities.steam.SteamAppInfoDeserializer;
import ms.gamerecommender.business.value.Game;

import javax.annotation.Nullable;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@JsonDeserialize(using = SteamAppInfoDeserializer.class)
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
