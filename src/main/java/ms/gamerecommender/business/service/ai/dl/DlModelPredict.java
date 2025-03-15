package ms.gamerecommender.business.service.ai.dl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.business.service.ai.AiPredict;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;

import java.util.List;

import static ms.gamerecommender.business.service.ai.dl.ModelUtils.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DlModelPredict implements AiPredict<Game, Float> {

    UserProfile trainUserProfile;
    int numberOfEpochs;

    @Override
    public Float predict(Game input) {
        return trainAndPredict(trainUserProfile, input, numberOfEpochs);
    }

    @Override
    public List<Float> batchPredict(List<Game> inputList) {
        return trainAndBatchPredict(trainUserProfile, inputList, numberOfEpochs);
    }
}
