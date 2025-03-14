package ms.gamerecommender.business.service.ai;

import lombok.experimental.UtilityClass;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;

import java.util.List;

@UtilityClass
public class DeepLearningUtilities {
    public List<FeatureVector> createFeatureVectorListForUser(UserProfile userProfile) {
        return userProfile.ownedGames().stream().map(FeatureVector::new).toList();
    }

    public ModelDataset createModelDatasetForUser(UserProfile userProfile) {
        return new ModelDataset(createFeatureVectorListForUser(userProfile));
    }

    public FeatureVector transformToFeatureVector(Game game) {
        return new FeatureVector(game);
    }
}
