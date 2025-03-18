package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.List;

import static ms.gamerecommender.business.service.RecommenderUtils.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@UtilityClass
class CollaborativeGameRecommender {
    @SneakyThrows
    List<Game> recommend(int userId, DataModel input, List<Game> dataset, int nearestNeighbors, int topN) {
        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(input);
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(nearestNeighbors, userSimilarity, input);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(input, userNeighborhood,  userSimilarity);

        val gameIds = recommender.recommend(userId, topN).stream().map(RecommendedItem::getItemID).toList();

        return dataset.stream().filter(
                game -> gameIds.contains((long) game.getGameId()) && game.getPositiveReviewPercentage() >= POSITIVE_REVIEW_PERCENTAGE_CUT_OFF
        ).toList();
    }
}
