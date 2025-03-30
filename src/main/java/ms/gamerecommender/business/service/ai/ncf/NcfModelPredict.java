package ms.gamerecommender.business.service.ai.ncf;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.business.service.ai.AiPredict;
import ms.gamerecommender.business.value.Game;
import org.apache.mahout.cf.taste.model.Preference;

import java.util.List;

import static ms.gamerecommender.business.service.ai.ncf.NcfModelUtils.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NcfModelPredict implements AiPredict<Game, Float> {

    int maxNumberOfUsers;
    int maxNumberOfGames;
    int numberOfEpochs;
    List<Preference> preferences;
    int userId;

    @Override
    public Float predict(Game input) {
        val ncfPoints = preferences.stream().map(it -> new NcfDataPoint((int) it.getUserID(), (int) it.getItemID(), it.getValue())).toList();
        return trainAndPredict(ncfPoints, userId, input.getGameId(), maxNumberOfUsers, maxNumberOfGames, numberOfEpochs);
    }

    @Override
    public List<Float> batchPredict(List<Game> inputList) {
        val ncfPoints = preferences.stream().map(it -> new NcfDataPoint((int) it.getUserID(), (int) it.getItemID(), it.getValue())).toList();
        return trainAndBatchPredict(
                ncfPoints, userId, inputList.stream().map(Game::getGameId).toList(),
                maxNumberOfUsers, maxNumberOfGames, numberOfEpochs
        );
    }
}
