package ms.gamerecommender.business.service.ai.dl;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ms.gamerecommender.business.service.ai.dl.ModelUtils.*;
import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.*;

class FeatureVectorTest {

    @Test
    void featureTransformTest() {
        val dataset = createDataset(1,1);
        val user = createUserProfile(1, dataset,1);

        val featureVector = transformToFeatureVector(user.ownedGames().getFirst());
        val expectedArray = new float[FeatureVector.VECTOR_SIZE];
        expectedArray[0] = (float) dataset.getFirst().getPositiveReviewPercentage() / 100;

        val index = FeatureVector.AVAILABLE_TAGS.indexOf(dataset.getFirst().getTags().toArray()[0]);
        expectedArray[index + 1] = 1;

        Assertions.assertEquals(expectedArray.length, featureVector.getFeatureArray().length);
        Assertions.assertArrayEquals(expectedArray, featureVector.getFeatureArray());

    }
}