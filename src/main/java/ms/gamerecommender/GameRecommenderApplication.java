package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO:
// HTMX do all services, (user done, maybe in user add providing steam key)
// Do this for some endpoint:
//          SteamApiService steamApiService = context.getBean(SteamApiService.class);
//          steamApiService.completeDatabase()
// Add missing exceptions to exceptionsHandler (InternalAuthenticationServiceException)
// Exception handling, Only errors for tags with htmx attributes, so if manually fetched it will not work, can be done like register endpoint, or just create specific responses in exceptionHandler, not generic one
// Remember to include id=error-modal in exception handlers responses
// (maybe) Saving model for NCF, just for learning, in RecommendService function getRecommendData(), check if there is file with it and than take it if present.
// ^ but for it to be successfully that file needs to be updated when user assigns score to the game, can be done when user assigns new score, but then there should be check if game was scored before
// ^ other easier option would be to retake table from database, but it will mean that it is the same as no saving at all
@SpringBootApplication
public class GameRecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}