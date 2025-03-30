package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO:
// Create service for exchaning info with steamApi (RestClient [I think, to check])
// ^ models for outputs and inputs from/to api and converters to Persistence entity
// Reload user data, use created merge function in userGameService
// Service to download data from steam In Insomnia necessary api calls, there is 237 thausend games, for tags need to combine categories and genres from appdetails endpoint and than filter by presence in all tags
// IPlayerService getOwnedGames (get appId and playtime_forever, rest of info propably get from dataset, maybe check if there is info about left review)
// Get App list, returns just app id with name of app, so to create dataset must be combined with get app info
// In get app info there is info about total recommendations, price, metacritic, categories + genres, rating, number of achievments
// Can get total negative, positive and total reviews from app review endpoint, app id is as path variable not query like above, can go through reviews via cursor to find if user reviewd (but probpably not useffull)
// HTMX for frontend (docs and learn)
// Think about saving model for NCF, just for learning
// Fix: make business value classes into interfaces (this will be more flexible, but need to create impl classes for tests( or add price, metacrit score to Game Class (this will remove AppGame)
// ^ Main goal is to remove GameEntity from this converter function convertToUserGameEntity
@SpringBootApplication
public class GameRecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}