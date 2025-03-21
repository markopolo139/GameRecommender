package ms.gamerecommender.app.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import ms.gamerecommender.app.persistence.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MyUserDetailsService implements UserDetailsService {

    UserProfileRepository userProfileRepository;

    @Autowired
    public MyUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = userProfileRepository.findByUsername(username);
        return null;
        //TODO: complete entity and fill this up
//        return new SecurityUser();
    }
}
