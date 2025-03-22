package ms.gamerecommender.app.persistence.repo;

import ms.gamerecommender.app.persistence.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {
    List<GameEntity> findByMetacriticScoreGreaterThan(double score);
    List<GameEntity> findByMetacriticScoreBetween(double lowScore, double highScore);
    List<GameEntity> findByPriceGreaterThan(double price);
    List<GameEntity> findByPriceBetween(double lowPrice, double highPrice);
}
