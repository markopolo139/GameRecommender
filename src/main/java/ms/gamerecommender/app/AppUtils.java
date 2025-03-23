package ms.gamerecommender.app;

import lombok.experimental.UtilityClass;
import ms.gamerecommender.app.entities.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AppUtils {
    public int getUserID() {
        return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
