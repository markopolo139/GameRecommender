package ms.gamerecommender.business.service.ai;

import lombok.val;
import ms.gamerecommender.business.excpetions.InvalidPredictModelException;
import ms.gamerecommender.business.service.ai.dl.DlModelPredict;
import ms.gamerecommender.business.service.ai.ncf.NcfModelPredict;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ms.gamerecommender.business.service.TestUtils.*;
import static ms.gamerecommender.business.service.ValueFactory.*;
import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.*;

class HybridAIGameRecommenderTest {

    @Test
    void invalidPredictModels() {
        val dataset = createDataset(50, 1);

        val userProfile = new UserProfile(1, List.of(
                createUserGame(dataset.get(5), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        ));

        val dlRecommender = new AiGameRecommender(
                new DlModelPredict(userProfile, 30)
        );

        val ncfRecommender = new AiGameRecommender(
                new NcfModelPredict(7, 50, 100, readCsvFile("src/test/resources/dataModelOneUser.csv"), 1)
        );

        Assertions.assertThrows(InvalidPredictModelException.class, () -> {
            new HybridAIGameRecommender(dlRecommender, dlRecommender);
        }, "ncfRecommender must use a NcfModelPredict");

        Assertions.assertThrows(InvalidPredictModelException.class, () -> {
            new HybridAIGameRecommender(ncfRecommender, ncfRecommender);
        }, "dlRecommender must use a DlModelPredict");

        Assertions.assertThrows(InvalidPredictModelException.class, () -> {
            new HybridAIGameRecommender(ncfRecommender, dlRecommender);
        }, "dlRecommender must use a DlModelPredict");
    }

    @Test
    void hybridRecommenderTest() {
        val dataset = createDataset(50, 1);

        val userProfile = new UserProfile(1, List.of(
                createUserGame(dataset.get(5), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        ));

        val dlRecommender = new AiGameRecommender(
                new DlModelPredict(userProfile, 30)
        );

        val ncfRecommender = new AiGameRecommender(
                new NcfModelPredict(7, 50, 100, readCsvFile("src/test/resources/dataModelOneUser.csv"), 1)
        );

        val recommender = new HybridAIGameRecommender(dlRecommender, ncfRecommender);

        val predictions = recommender.recommend(userProfile, dataset, 10);

        Assertions.assertFalse(predictions.isEmpty());
    }
}