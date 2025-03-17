package ms.gamerecommender.business.service.ai;

import lombok.val;
import ms.gamerecommender.business.service.ai.dl.DlModelPredict;
import ms.gamerecommender.business.service.ai.ncf.NcfModelPredict;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ms.gamerecommender.business.service.ValueFactory.*;
import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.*;

class AiGameRecommenderTest {

    @Test
    void dlModelPredictTest() {
        val dataset = createDataset(50, 1);

        val userProfile = new UserProfile(1, List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(5), 35, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(6), 262, 4, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(7), 500, 1, UserGame.Review.NEGATIVE),
                createUserGame(dataset.get(8), 150, 3, UserGame.Review.NEGATIVE),
                createUserGame(dataset.get(9), 15, 5, UserGame.Review.POSITIVE)
        ));

        val recommender = new AiGameRecommender(
                new DlModelPredict(userProfile, 30)
        );

        val predictions = recommender.recommend(userProfile, dataset, 5);

        Assertions.assertFalse(predictions.isEmpty());
        Assertions.assertEquals(5, predictions.size());
    }

    @Test
    void ncfModelPredictTest() {
        val dataset = createDataset(50, 1);

        val userProfile = new UserProfile(1, List.of(
                createUserGame(dataset.get(5), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        ));

        val recommender = new AiGameRecommender(
                new NcfModelPredict(7, 50, 100, "src/test/resources/dataModelOneUser.csv", 1)
        );

        val predictions = recommender.recommend(userProfile, dataset, 5);

        Assertions.assertFalse(predictions.isEmpty());
        Assertions.assertEquals(5, predictions.size());
    }
}