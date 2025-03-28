package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import ms.gamerecommender.app.exceptions.InvalidGameIdException;
import ms.gamerecommender.app.persistence.GameEntity;
import ms.gamerecommender.app.persistence.UserGameEntity;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.app.persistence.repo.GameRepository;
import ms.gamerecommender.app.persistence.repo.UserGameRepository;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import ms.gamerecommender.business.value.UserGame;
import org.springframework.stereotype.Service;

import static ms.gamerecommender.app.AppUtils.*;
import static ms.gamerecommender.app.entities.AppConverters.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Log4j2
public class UserGameService {
    UserGameRepository userGameRepository;
    GameRepository gameRepository;
    UserProfileRepository userProfileRepository;

    int userId = getUserID();

    public void addGame(int gameId, double timePlayed, double rating, UserGame.Review review) {
        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(() -> {
            log.info("Game with id {} not found, request from user {}", gameId, userId);
            return new InvalidGameIdException("Game with id " + gameId + " not found");
        });

        UserProfileEntity userProfile = userProfileRepository.getReferenceById(userId);

        UserGameEntity userGameEntity = new UserGameEntity(userProfile, gameEntity, timePlayed, rating, review, 0);

        userGameEntity.setScore(convertToUserGame(userGameEntity).calculateScore());
        userGameRepository.save(userGameEntity);
    }


}
