package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO: Recommendation algorithm (content-based + collaborative) and AI (neural network)

@SpringBootApplication
public class GameRecommenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }

}
