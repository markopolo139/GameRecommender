package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO:
// Config for spring security
// Create in app config for creating beans for recommenders
// For now dataModel from mahoot and for NFC Model will be read from csv file, csv created during loading of user games via api and then saving in resources
//  and this file will be updated each time some user makes changes to their owned games or ranking or updates time played,
//  maybe it will be easier to store score in column of table for games instead of calculating it for all owned games each time new game is added
// Quick database setup
// In app layer add metacritic score and price in games , both though can be used for sorting or displaying in gui
// Create all necessary entities, repos and services for CRUD with data and converters
// Create service for exchaning info with steamApi
// Service for logging in to the app (userDetailsService), open old project for quick check
// Create serivce for adding ratings and reviews
// Create service which will output recommendations (one for algorithmic and one for AI)
// Service to download data from steam In Insomnia necessary api calls, there is 237 thausend games, for tags need to combine categories and genres from appdetails endpoint and than filter by presence in all tags
// Get tags is just list of tags with tagID and name
// IPlayerService getOwnedGames (get appId and playtime_forever, rest of info propably get from dataset, maybe check if there is info about left review)
// Get App list, returns just app id with name of app, so to create dataset must be combined with get app info
// In get app info there is info about total recommendations, price, metacritic, categories + genres, rating, number of achievments
// Can get total negative, positive and total reviews from app review endpoint, app id is as path variable not query like above, can go through reviews via cursor to find if user reviewd (but probpably not useffull)
// HTMX for frontend (docs and learn)
// Think about saving model for NCF
// Test other models and as input for AI models that maybe pair Game and userID
// FeatureVector use Sparse representation for vector
@SpringBootApplication
public class GameRecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}