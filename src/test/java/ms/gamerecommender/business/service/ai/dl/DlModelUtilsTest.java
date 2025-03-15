package ms.gamerecommender.business.service.ai.dl;

import lombok.val;
import ms.gamerecommender.business.value.UserGame;
import org.junit.jupiter.api.Test;

import static ms.gamerecommender.business.service.ai.dl.DlModelUtils.trainAndBatchPredict;
import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.createDataset;
import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.createUserProfile;

class DlModelUtilsTest {

    @Test
    void testIfModelIsConfiguredCorrectly() {
        val dataset = createDataset(10000,3);
        val user = createUserProfile(1, dataset,300);

        val notPlayedGames = dataset.stream().filter(
                game -> user.ownedGames().stream().allMatch(userGame -> userGame.getGameId() != game.getGameId())
        ).toList();

        val userScores = user.ownedGames().stream().map(UserGame::calculateScore).toList();

        val predicitons = trainAndBatchPredict(user, notPlayedGames, 30);

        System.out.println();
    }
}