package ms.gamerecommender;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;

//TODO: Recommendation algorithm (content-based + collaborative) and AI (neural network)
// FOr mahoot data must be in form userID, itemID, score
// Do 2 AI models, deep network, and neural collaborative
// For now dataaModel from mahoot will be read from csv file
// For tests create own csv file and substitate properites file
@SpringBootApplication
@Log4j2
public class GameRecommenderApplication {
    public static void main(String[] args) {
        log.error("TEST");
        log.info("TEST");
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}
