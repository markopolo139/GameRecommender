package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ms.gamerecommender.app.entities.SecurityUser;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class MyUserDetailsService implements UserDetailsService {

    UserProfileRepository userProfileRepository;

    public MyUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            val user = userProfileRepository.findByUsername(username).get();
            return new SecurityUser(
                    user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")), user.getId()
            );
        } catch (UsernameNotFoundException e) {
            log.error("Given username is not present");
            throw new UsernameNotFoundException("Given username is not present");
        }
    }
}
