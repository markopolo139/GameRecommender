package ms.gamerecommender.app.entities;

import lombok.experimental.UtilityClass;
import ms.gamerecommender.app.persistence.GameEntity;
import ms.gamerecommender.app.persistence.UserGameEntity;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AppConverters {
    public AppGame convertToAppGame(GameEntity gameEntity) {
        return new AppGame(
                gameEntity.getId(), gameEntity.getTitle(), gameEntity.getTags(), gameEntity.getPositiveReviewPercentage(),
                gameEntity.getMetacriticScore(), gameEntity.getPrice()

        );
    }

    public List<AppGame> convertToAppGameList(List<GameEntity> gameEntities) {
        return gameEntities.stream().map(AppConverters::convertToAppGame).toList();
    }

    public GameEntity convertToGameEntity(AppGame appGame) {
        return new GameEntity(
                appGame.getGameId(), appGame.getTitle(), appGame.getTags(),
                appGame.getPositiveReviewPercentage(), appGame.getMetacriticScore(), appGame.getPrice()
        );
    }

    public List<GameEntity> convertToGameEntityList(List<AppGame> appGames) {
        return appGames.stream().map(AppConverters::convertToGameEntity).toList();
    }

    public UserGame convertToUserGame(UserGameEntity userGameEntity) {
        return new UserGame(
                convertToAppGame(userGameEntity.getGameEntity()),
                userGameEntity.getTimePlayed(), userGameEntity.getRating(), userGameEntity.getReview()
        );
    }

    public Set<UserGame> convertToUserGameSet(Set<UserGameEntity> userGameEntities) {
        return userGameEntities.stream().map(AppConverters::convertToUserGame).collect(Collectors.toSet());
    }

    public UserGameEntity convertToUserGameEntity(UserProfileEntity userProfileEntity, GameEntity gameEntity, UserGame userGame) {
        return new UserGameEntity(
                userProfileEntity, gameEntity, userGame.getTimePlayed(),
                userGame.getRating(), userGame.getReview(), userGame.calculateScore()
        );
    }

    public Set<UserGameEntity> convertToUserGameEntityList(UserProfileEntity userProfileEntity, Set<Pair<GameEntity, UserGame>> userGames) {
        return userGames.stream().map(
                pair -> convertToUserGameEntity(userProfileEntity, pair.getFirst(), pair.getSecond())
        ).collect(Collectors.toSet());
    }

    public UserProfile convertToUserProfile(UserProfileEntity userProfileEntity) {
        return new UserProfile(
                userProfileEntity.getId(), convertToUserGameSet(userProfileEntity.getUserGames()).stream().toList()
        );
    }
}
