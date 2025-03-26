package ms.gamerecommender.app.persistence.repo;

import ms.gamerecommender.app.persistence.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {
    Optional<UserProfileEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
