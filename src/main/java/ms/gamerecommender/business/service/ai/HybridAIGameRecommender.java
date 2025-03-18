package ms.gamerecommender.business.service.ai;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.business.excpetions.InvalidPredictModelException;
import ms.gamerecommender.business.service.Recommender;
import ms.gamerecommender.business.service.ai.dl.DlModelPredict;
import ms.gamerecommender.business.service.ai.ncf.NcfModelPredict;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;

import java.util.LinkedHashSet;
import java.util.List;

import static ms.gamerecommender.business.service.RecommenderUtils.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HybridAIGameRecommender implements Recommender<UserProfile, Game> {

    AiGameRecommender dlRecommender;
    AiGameRecommender ncfRecommender;

    public HybridAIGameRecommender(AiGameRecommender dlRecommender, AiGameRecommender ncfRecommender) {
        if (!(dlRecommender.getPredictModel() instanceof DlModelPredict)) {
            throw new InvalidPredictModelException("dlRecommender must use a DlModelPredict");
        }

        if (!(ncfRecommender.getPredictModel() instanceof NcfModelPredict)) {
            throw new InvalidPredictModelException("ncfRecommender must use a NcfModelPredict");
        }

        this.dlRecommender = dlRecommender;
        this.ncfRecommender = ncfRecommender;
    }

    @Override
    @SneakyThrows
    public List<Game> recommend(UserProfile input, List<Game> dataset, int topN) {
        val howManyRecommend = (int) Math.round(TOP_N_FACTOR * topN);

        val collaborativeRecommendations = dlRecommender.recommend(input, dataset, howManyRecommend);
        val contentBasedRecommendations = ncfRecommender.recommend(input, dataset, howManyRecommend);

        val result = new LinkedHashSet<Game>();
        result.addAll(collaborativeRecommendations);
        result.addAll(contentBasedRecommendations);

        return result.stream().limit(topN).toList();
    }
}
