package ms.gamerecommender.business.service.ai;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.business.service.Recommender;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ms.gamerecommender.business.DatasetUtility.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiGameRecommender implements Recommender<UserProfile, Game> {

    AiPredict<Game, Float> predictModel;

    @Override
    public List<Game> recommend(UserProfile input, List<Game> dataset, int topN) {
        val notOwnedGames = removeOwnedGamesFromDatabase(dataset, input.ownedGames());
        val predictScores = predictModel.batchPredict(notOwnedGames);

        val combinedGames = new ArrayList<Pair<Game, Float>>();

        for (int i = 0; i < notOwnedGames.size(); i++) {
            combinedGames.add(Pair.of(notOwnedGames.get(i), predictScores.get(i)));
        }

        return combinedGames.stream()
                .sorted(Comparator.comparingDouble(Pair::getRight))
                .map(Pair::getLeft)
                .toList();
    }
}
