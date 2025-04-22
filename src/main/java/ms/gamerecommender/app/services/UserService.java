package ms.gamerecommender.app.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ms.gamerecommender.app.entities.SecurityUser;
import ms.gamerecommender.app.exceptions.AppException;
import ms.gamerecommender.app.exceptions.IncorrectPasswordException;
import ms.gamerecommender.app.exceptions.UsernameUsedException;
import ms.gamerecommender.app.persistence.UserProfileEntity;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static ms.gamerecommender.app.AppUtils.*;

@Service
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserService {
    UserProfileRepository repository;
    PasswordEncoder encoder;

    public void createUser(String username, String password) {
        if (repository.existsByUsername(username)) {
            log.info("Given username ({}) is already present in database", username);
            throw new UsernameUsedException("Username is already in use");
        }

        repository.save(new UserProfileEntity(username, encoder.encode(password)));
    }

    public void updateUsername(String username, String password, HttpServletRequest request) {
        val user = repository.findById(getUserID()).orElseThrow(() -> new AppException("Unexpected Error"));

        if (repository.existsByUsername(username)) {
            log.info("User provided new username ({}) is already present in database", username);
            throw new UsernameUsedException("Username is already in use");
        }

        if (!encoder.matches(password, user.getPassword())) {
            log.info("User provided password does not match password in database (userID: {})", getUserID());
            throw new IncorrectPasswordException("User provided invalid password");
        }

        user.setUsername(username);
        repository.save(user);

        val securityUser = new SecurityUser(
                user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")), user.getId()
        );

        AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                securityUser, SecurityContextHolder.getContext().getAuthentication().getCredentials(), securityUser.getAuthorities()
        );

        auth.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.changeSessionId();
    }

    public void updatePassword(String oldPassword, String newPassword) {
        val user = repository.findById(getUserID()).orElseThrow(() -> new AppException("Unexpected Error"));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            log.info("User provided old password does not match old password in database (userID: {})", getUserID());
            throw new IncorrectPasswordException("User provided invalid old password");
        }

        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }
}
