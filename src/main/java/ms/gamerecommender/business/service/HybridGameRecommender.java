package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static ms.gamerecommender.business.service.RecommenderUtils.*;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HybridGameRecommender implements Recommender<UserProfile, Game> {
    Integer nearestNeighbors;
    List<Preference> preferences;

    @Override
    @SneakyThrows
    public List<Game> recommend(UserProfile input, List<Game> dataset, int topN) {
        val howManyRecommend = (int) Math.round(TOP_N_FACTOR * topN);

        val collaborativeRecommendations = CollaborativeGameRecommender.recommend(
                input.userId(), new GenericDataModel(convertPreferencesToArray(preferences)), dataset, nearestNeighbors, howManyRecommend
        );

        val contentBasedRecommendations = ContentBasedGameRecommender.recommend(input.ownedGames(), dataset, howManyRecommend);

        val result = new LinkedHashSet<Game>();
        result.addAll(collaborativeRecommendations);
        result.addAll(contentBasedRecommendations);

        return result.stream().limit(topN).toList();
    }

    private FastByIDMap<PreferenceArray> convertPreferencesToArray(List<Preference> preferences) {
        FastByIDMap<Collection<Preference>> tempUserData = new FastByIDMap<>();
        for (Preference pref : preferences) {
            long userID = pref.getUserID();
            var userPrefs = tempUserData.get(userID);

            if (userPrefs == null) {
                userPrefs = new ArrayList<>();
                userPrefs.add(pref);
                tempUserData.put(userID, userPrefs);
            }
            else {
                userPrefs.add(pref);
            }
        }

        return GenericDataModel.toDataMap(tempUserData, true);
    }
}
