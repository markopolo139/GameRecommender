package ms.gamerecommender.app.services;

import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ms.gamerecommender.app.exceptions.InvalidGameIdException;
import ms.gamerecommender.app.persistence.GameEntity;
import ms.gamerecommender.app.persistence.UserGameEntity;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.app.persistence.repo.GameRepository;
import ms.gamerecommender.app.persistence.repo.UserGameRepository;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import ms.gamerecommender.business.value.UserGame;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

import static ms.gamerecommender.app.AppUtils.*;
import static ms.gamerecommender.app.entities.AppConverters.*;
import static ms.gamerecommender.app.services.FilterBoundValues.*;
import static org.apache.commons.lang3.ObjectUtils.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Log4j2
public class UserGameService {
    UserGameRepository userGameRepository;
    GameRepository gameRepository;
    UserProfileRepository userProfileRepository;

    int userId = getUserID();

    public void addGameToUserProfile(int gameId, double timePlayed, @Nullable Double rating, @Nullable UserGame.Review review) {

        val gameEntity = gameRepository.findById(gameId).orElseThrow(() -> {
            log.info("Adding Game with id {} not found in table, request from user {}", gameId, userId);
            return new InvalidGameIdException("Game with id " + gameId + " not found");
        });

        val userProfile = getCurrentUserProfileReference();

        val userGameEntity = new UserGameEntity(
                userProfile, gameEntity, timePlayed, defaultIfNull(rating, 0.0), defaultIfNull(review, UserGame.Review.NONE), 0
        );

        userGameEntity.setScore(convertToUserGame(userGameEntity).calculateScore());
        userGameRepository.save(userGameEntity);
    }

    public void updateUserRatingAndReviewForOwnedGame(int gameId, @Nullable Double rating, @Nullable UserGame.Review review) {
        val gameToUpdate = getCurrentUserProfileReference().getUserGames().stream().filter(game -> game.getGameEntity().getId() == gameId)
                .findFirst().orElseThrow(
                        () -> {
                            log.info("Updating Game with id {} not present in user owned games, request from user {}", gameId, userId);
                            return new InvalidGameIdException("Game with id " + gameId + " not found");
                        }
        );

        val ratingToSet = defaultIfNull(rating, gameToUpdate.getRating());
        val reviewToSet = defaultIfNull(review, gameToUpdate.getReview());

        gameToUpdate.setRating(ratingToSet);
        gameToUpdate.setReview(reviewToSet);

        gameToUpdate.setScore(convertToUserGame(gameToUpdate).calculateScore());

        userGameRepository.save(gameToUpdate);
    }

    public void removeOwnedGame(int gameId) {
        val user = getCurrentUserProfile();
        user.getUserGames().removeIf(game -> game.getGameEntity().getId() == gameId);
        userProfileRepository.save(user);
    }

    //TODO: yuck, entity shouldn't be used here, but I'am lazy, maybe fix with this done (make business value classes into interfaces)
    public List<UserGameEntity> getOwnedGames(
            @Nullable Double priceLowBound, @Nullable Double priceHighBound, @Nullable Double positivePercentLowBound,
            @Nullable Double positivePercentHighBound, @Nullable Double metacriticScoreLowBound, @Nullable Double metacriticScoreHighBound,
            @Nullable Double timePlayedLowBound, @Nullable Double timePlayedHighBound, @Nullable Double ratingLowBound,
            @Nullable Double ratingHighBound, @Nullable Double scoreLowBound, @Nullable Double scoreHighBound,
            @Nullable String titleRegex, @Nullable UserGame.Review review
    ) {
        val priceLowBoundFilter = defaultIfNull(priceLowBound, PRICE_LOW_BOUND);
        val priceHighBoundFilter = defaultIfNull(priceHighBound, PRICE_HIGH_BOUND);
        val positivePercentLowBoundFilter = defaultIfNull(positivePercentLowBound, POSITIVE_REVIEW_PERCENTAGE_LOW_BOUND);
        val positivePercentHighBoundFilter = defaultIfNull(positivePercentHighBound, POSITIVE_REVIEW_PERCENTAGE_HIGH_BOUND);
        val metacriticScoreLowBoundFilter = defaultIfNull(metacriticScoreLowBound, METACRITIC_SCORE_LOW_BOUND);
        val metacriticScoreHighBoundFilter = defaultIfNull(metacriticScoreHighBound, METACRITIC_SCORE_HIGH_BOUND);

        val timePlayedLowBoundFilter = defaultIfNull(timePlayedLowBound, TIME_PLAYED_LOW_BOUND);
        val timePlayedHighBoundFilter = defaultIfNull(timePlayedHighBound, TIME_PLAYED_HIGH_BOUND);
        val ratingLowBoundFilter = defaultIfNull(ratingLowBound, RATING_LOW_BOUND);
        val ratingHighBoundFilter = defaultIfNull(ratingHighBound, RATING_HIGH_BOUND);
        val scoreLowBoundFilter = defaultIfNull(scoreLowBound, SCORE_LOW_BOUND);
        val scoreHighBoundFilter = defaultIfNull(scoreHighBound, SCORE_HIGH_BOUND);

        Specification<UserGameEntity> spec = Specification.where((
                (root, query, cb) -> {
                    Join<UserGameEntity, UserProfileEntity> userJoin = root.join("userProfileEntity");
                    return cb.equal(userJoin.get("id"), userId);
                })
        );

        //Spec for user game entity
        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timePlayed"), timePlayedLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timePlayed"), timePlayedHighBoundFilter));
        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), ratingLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("rating"), ratingHighBoundFilter));
        spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("score"), scoreLowBoundFilter));
        spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("score"), scoreHighBoundFilter));

        if (review != null) {
            spec.and((root, query, cb) -> cb.equal(root.get("review"), review));
        }

        //Spec for game entity
        if (titleRegex != null) {
            spec.and((root, query, cb) -> {
                Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
                return cb.like(gameJoin.get("title"), titleRegex);
            });
        }

        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.greaterThanOrEqualTo(gameJoin.get("positiveReviewPercentage"), positivePercentLowBoundFilter);
        });
        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.lessThanOrEqualTo(gameJoin.get("positiveReviewPercentage"), positivePercentHighBoundFilter);
        });
        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.greaterThanOrEqualTo(gameJoin.get("metacriticScore"), metacriticScoreLowBoundFilter);
        });
        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.lessThanOrEqualTo(gameJoin.get("metacriticScore"), metacriticScoreHighBoundFilter);
        });
        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.greaterThanOrEqualTo(gameJoin.get("price"), priceLowBoundFilter);
        });
        spec.and((root, query, cb) -> {
            Join<UserGameEntity, GameEntity> gameJoin = root.join("gameEntity");
            return cb.lessThanOrEqualTo(gameJoin.get("price"), priceHighBoundFilter);
        });

        return userGameRepository.findAll(spec);
    }

    private UserProfileEntity getCurrentUserProfileReference() {
        return userProfileRepository.getReferenceById(userId);
    }

    private UserProfileEntity getCurrentUserProfile() {
        return userProfileRepository.findById(userId).orElseThrow();
    }
}
