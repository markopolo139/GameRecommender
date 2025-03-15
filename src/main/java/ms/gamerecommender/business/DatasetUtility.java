package ms.gamerecommender.business;

import lombok.experimental.UtilityClass;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;

import java.util.List;

@UtilityClass
public class DatasetUtility {
    public boolean checkIfGamePresentInOwnedGames(Game game, List<UserGame> playedGames) {
        return playedGames.stream().map(Game::getGameId).noneMatch(gameId -> gameId.equals(game.getGameId()));
    }

    public List<Game> removeOwnedGamesFromDatabase(List<Game> dataset, List<UserGame> playedGames) {
        return dataset.stream().filter((game -> checkIfGamePresentInOwnedGames(game, playedGames))).toList();
    }
}
