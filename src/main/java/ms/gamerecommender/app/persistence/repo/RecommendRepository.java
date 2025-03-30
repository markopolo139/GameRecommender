package ms.gamerecommender.app.persistence.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.app.persistence.GameEntity;
import ms.gamerecommender.app.persistence.RecommendDTO;
import ms.gamerecommender.app.persistence.UserGameEntity;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendRepository {

    @PersistenceContext
    EntityManager em;

    public List<RecommendDTO> getRecommendData() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RecommendDTO> cq = cb.createQuery(RecommendDTO.class);

        Root<UserProfileEntity> userRoot = cq.from(UserProfileEntity.class);
        Join<UserProfileEntity, UserGameEntity> userGameRoot = userRoot.join("userGames");
        Join<UserGameEntity, GameEntity> gameRoot = userGameRoot.join("gameEntity");

        cq.select(cb.construct(
                RecommendDTO.class,
                userRoot.get("id"),
                gameRoot.get("id"),
                userGameRoot.get("score")
        ));

        return em.createQuery(cq).getResultList();
    }

}
