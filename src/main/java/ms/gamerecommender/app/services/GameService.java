package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.app.entities.AppGame;
import ms.gamerecommender.app.persistence.GameEntity;
import ms.gamerecommender.app.persistence.repo.GameRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

import static ms.gamerecommender.app.entities.AppConverters.*;
import static ms.gamerecommender.app.services.FilterBoundValues.*;
import static org.apache.commons.lang3.ObjectUtils.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class GameService {
    GameRepository gameRepository;

    public List<AppGame> getAllGames(
            @Nullable Double priceLowBound, @Nullable Double priceHighBound, @Nullable Double positivePercentLowBound,
            @Nullable Double positivePercentHighBound, @Nullable Double metacriticScoreLowBound, @Nullable Double metacriticScoreHighBound,
            @Nullable String titleRegex
    ) {
        val priceLowBoundFilter = defaultIfNull(priceLowBound, PRICE_LOW_BOUND);
        val priceHighBoundFilter = defaultIfNull(priceHighBound, PRICE_HIGH_BOUND);
        val positivePercentLowBoundFilter = defaultIfNull(positivePercentLowBound, POSITIVE_REVIEW_PERCENTAGE_LOW_BOUND);
        val positivePercentHighBoundFilter = defaultIfNull(positivePercentHighBound, POSITIVE_REVIEW_PERCENTAGE_HIGH_BOUND);
        val metacriticScoreLowBoundFilter = defaultIfNull(metacriticScoreLowBound, METACRITIC_SCORE_LOW_BOUND);
        val metacriticScoreHighBoundFilter = defaultIfNull(metacriticScoreHighBound, METACRITIC_SCORE_HIGH_BOUND);

        Specification<GameEntity> spec = Specification.where(null);

        //Spec for game entity
        if (titleRegex != null) {
            spec.and((root, query, cb) -> cb.like(root.get("title"), titleRegex));
        }

        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("positiveReviewPercentage"), positivePercentLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("positiveReviewPercentage"), positivePercentHighBoundFilter));
        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("metacriticScore"), metacriticScoreLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("metacriticScore"), metacriticScoreHighBoundFilter));
        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), priceLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), priceHighBoundFilter));

        return convertToAppGameList(gameRepository.findAll(spec));
    }
}
