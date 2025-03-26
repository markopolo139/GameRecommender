package ms.gamerecommender.app;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import ms.gamerecommender.app.entities.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
@UtilityClass
public class AppUtils {
    public int getUserID() {
        try {
            return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        } catch (Exception e) {
            log.error("User is not logged in, or tries to get to resource not for him");
            throw e;
        }
    }
}
