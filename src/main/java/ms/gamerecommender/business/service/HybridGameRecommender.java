package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HybridGameRecommender implements Recommender<UserProfile, Game> {

    @NonFinal
    @Value("${recommender.data.model.csvPath}")
    String csvPath;

    @Override
    @SneakyThrows
    public List<Game> recommend(UserProfile input, List<Game> dataset, int topN) {
        val collaborativeRecommendations = CollaborativeGameRecommender.recommend(
                input.userId(), new FileDataModel(new File(csvPath)),dataset, Math.floorDiv(topN, 2)
        );
        val contentBasedRecommendations = ContentBasedGameRecommender.recommend(input.ownedGames(), dataset, Math.ceilDiv(topN, 2));

        val result = new LinkedHashSet<Game>();
        result.addAll(collaborativeRecommendations);
        result.addAll(contentBasedRecommendations);

        return result.stream().limit(topN).toList();
    }
}
