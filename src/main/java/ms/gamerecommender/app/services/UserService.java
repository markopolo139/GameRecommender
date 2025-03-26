package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ms.gamerecommender.app.exceptions.AppException;
import ms.gamerecommender.app.exceptions.IncorrectPasswordException;
import ms.gamerecommender.app.exceptions.UsernameUsedException;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static ms.gamerecommender.app.AppUtils.*;

@Service
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserService {
    UserProfileRepository repository;
    PasswordEncoder encoder;

    private int userId = getUserID();

    public void createUser(String username, String password) {
        if (repository.existsByUsername(username)) {
            log.info("Given username ({}) is already present in database", username);
            throw new UsernameUsedException("Username is already in use");
        }

        repository.save(new UserProfileEntity(username, encoder.encode(password)));
    }

    public void updateUsername(String username) {
        val user = repository.findById(userId).orElseThrow(() -> new AppException("Unexpected Error"));

        if (repository.existsByUsername(username)) {
            log.info("User provided new username ({}) is already present in database", username);
            throw new UsernameUsedException("Username is already in use");
        }

        user.setUsername(username);
        repository.save(user);
    }

    public void updatePassword(String oldPassword, String newPassword) {
        val user = repository.findById(userId).orElseThrow(() -> new AppException("Unexpected Error"));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            log.info("User provided old password does not match old password in database (userID: {})", userId);
            throw new IncorrectPasswordException("User provided invalid old password");
        }

        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }
}
