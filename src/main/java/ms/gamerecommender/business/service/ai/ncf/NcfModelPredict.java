package ms.gamerecommender.business.service.ai.ncf;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.business.service.ai.AiPredict;
import ms.gamerecommender.business.value.Game;

import java.util.List;

import static ms.gamerecommender.business.service.ai.ncf.NcfModelUtils.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NcfModelPredict implements AiPredict<Game, Float> {

    int maxNumberOfUsers;
    int maxNumberOfGames;
    int numberOfEpochs;
    String csvDataPath;
    int userId;

    @Override
    public Float predict(Game input) {
        return trainAndPredict(csvDataPath, userId, input.getGameId(), maxNumberOfUsers, maxNumberOfGames, numberOfEpochs);
    }

    @Override
    public List<Float> batchPredict(List<Game> inputList) {
        return trainAndBatchPredict(
                csvDataPath, userId, inputList.stream().map(Game::getGameId).toList(),
                maxNumberOfUsers, maxNumberOfGames, numberOfEpochs
        );
    }
}
