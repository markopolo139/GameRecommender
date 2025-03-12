package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO: Recommendation algorithm (content-based + collaborative) [Tests] and AI (neural network)
// Do 2 AI models, deep network, and neural collaborative
// For now dataaModel from mahoot will be read from csv file, csv created during loading of user games via api and then saving in resources
//  and this file will be updated each time some user makes changes to their owned games or ranking or updates time played,
//  maybe it will be easier to store score in column of table for games instead of calculating it for all owned games each time new game is added
// For tests create own csv file and substitate properites file
//  Create in app config for creating beans for recmoennders
@SpringBootApplication
public class GameRecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}
