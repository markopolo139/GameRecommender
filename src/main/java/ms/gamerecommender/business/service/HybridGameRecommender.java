package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HybridGameRecommender implements Recommender<UserProfile, Game> {

    static double TOP_N_FACTOR = 0.75;

    Integer nearestNeighbors;
    String csvPath;

    @Override
    @SneakyThrows
    public List<Game> recommend(UserProfile input, List<Game> dataset, int topN) {
        val howManyRecommend = (int) Math.round(TOP_N_FACTOR * topN);

        val collaborativeRecommendations = CollaborativeGameRecommender.recommend(
                input.userId(), new FileDataModel(new File(csvPath)),dataset, nearestNeighbors, howManyRecommend
        );
        val contentBasedRecommendations = ContentBasedGameRecommender.recommend(input.ownedGames(), dataset, howManyRecommend);

        val result = new LinkedHashSet<Game>();
        result.addAll(collaborativeRecommendations);
        result.addAll(contentBasedRecommendations);

        return result.stream().limit(topN).toList();
    }
}
