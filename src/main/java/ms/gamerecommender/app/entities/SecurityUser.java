package ms.gamerecommender.app.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityUser extends User {
    @Getter(AccessLevel.PUBLIC)
    int id;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, int id) {
        super(username, password, authorities);
        this.id = id;
    }
}
