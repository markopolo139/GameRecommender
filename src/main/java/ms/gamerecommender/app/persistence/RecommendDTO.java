package ms.gamerecommender.app.persistence;

import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.model.Preference;

public record RecommendDTO(int userId, int gameId, float score) {
    public Preference toPreference() {
        return new GenericPreference(userId, gameId, score);
    }
}
