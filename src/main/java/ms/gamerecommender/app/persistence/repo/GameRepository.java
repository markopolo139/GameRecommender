package ms.gamerecommender.app.persistence.repo;

import ms.gamerecommender.app.persistence.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer>, JpaSpecificationExecutor<GameEntity> { }
