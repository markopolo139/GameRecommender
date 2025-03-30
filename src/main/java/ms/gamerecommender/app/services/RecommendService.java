package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import ms.gamerecommender.app.entities.AppGame;
import ms.gamerecommender.app.persistence.RecommendDTO;
import ms.gamerecommender.app.persistence.repo.GameRepository;
import ms.gamerecommender.app.persistence.repo.RecommendRepository;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import ms.gamerecommender.business.service.HybridGameRecommender;
import ms.gamerecommender.business.service.Recommender;
import ms.gamerecommender.business.service.ai.AiGameRecommender;
import ms.gamerecommender.business.service.ai.HybridAIGameRecommender;
import ms.gamerecommender.business.service.ai.dl.DlModelPredict;
import ms.gamerecommender.business.service.ai.ncf.NcfModelPredict;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.mahout.cf.taste.model.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static ms.gamerecommender.app.AppUtils.*;
import static ms.gamerecommender.app.entities.AppConverters.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RecommendService {
    GameRepository gameRepository;
    UserProfileRepository userProfileRepository;
    RecommendRepository recommendRepository;

    @NonFinal
    @Value("${recommender.algorithm.nearest.neighbours}")
    int nearestNeighbours;

    @NonFinal
    @Value("${recommender.ai.dl.training.epochs}")
    int dlTrainingEpochs;

    @NonFinal
    @Value("${recommender.ai.ncf.training.epochs}")
    int ncfTrainingEpochs;

    int userId = getUserID();

    public RecommendService(GameRepository gameRepository, UserProfileRepository userProfileRepository, RecommendRepository recommendRepository) {
        this.gameRepository = gameRepository;
        this.userProfileRepository = userProfileRepository;
        this.recommendRepository = recommendRepository;
    }

    public List<AppGame> aiRecommend(int topN) {
        val dlRecommender = new AiGameRecommender(
                new DlModelPredict(getCurrentUserProfile(), dlTrainingEpochs)
        );

        val ncfRecommender = new AiGameRecommender(
                new NcfModelPredict(
                        (int) userProfileRepository.count(), (int) gameRepository.count(), ncfTrainingEpochs, getRecommendData(), userId
                )
        );

        val recommender = new HybridAIGameRecommender(dlRecommender, ncfRecommender);

        return recommender.recommend(getCurrentUserProfile(), getDataset(), topN).stream().map(
                it -> (AppGame) it
        ).toList();
    }

    public List<AppGame> algorithmRecommend(int topN) {
        Recommender<UserProfile, Game> recommender = new HybridGameRecommender(nearestNeighbours, getRecommendData());

        return recommender.recommend(getCurrentUserProfile(), getDataset(), topN).stream().map(
                it -> (AppGame) it
        ).toList();
    }

    private List<Preference> getRecommendData() {
        return recommendRepository.getRecommendData().stream().map(RecommendDTO::toPreference).toList();
    }

    private List<Game> getDataset() {
        return gameRepository.findAll().stream().map(it -> (Game) convertToAppGame(it)).toList();
    }

    private UserProfile getCurrentUserProfile() {
        return convertToUserProfile(userProfileRepository.findById(userId).orElseThrow());
    }
}
