package ms.gamerecommender.business.service.ai;

import lombok.val;
import org.junit.jupiter.api.Test;

import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.*;

class FeatureVectorTest {

    @Test
    void featureTransformTest() {
        val dataset = createDataset(1,1);
        val user = createUserProfile(1,dataset,1);
//64.11 3.35 POSITIVE
        System.out.println();
    }
}