package ms.gamerecommender.app.persistence.repo;

import ms.gamerecommender.app.persistence.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {
    UserProfileEntity findByUsername(String username);
}
