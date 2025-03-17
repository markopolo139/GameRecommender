package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO:
// Test other models and as input for AI models that maybe pair Game and userID
// Play around with steam api using insomnia
// IStoreServicce has app details and tag list, docs: https://steamapi.xpaw.me/#IStoreService/GetTagList
// ISteamUserStats for stats of user for the game
// IPlayerService getOwnedGames
// review: https://partner.steamgames.com/doc/store/getreviews
// Some way to get review number for games: https://stackoverflow.com/questions/53451458/get-positive-and-negative-review-from-steam-api
// If found something interesting update model with new found data
// Put to FeatureVector all avaiable tags when extracted from SteamAPI
// Config for spring security
// Create in app config for creating beans for recommenders
// For now dataaModel from mahoot and for NFC Model will be read from csv file, csv created during loading of user games via api and then saving in resources
//  and this file will be updated each time some user makes changes to their owned games or ranking or updates time played,
//  maybe it will be easier to store score in column of table for games instead of calculating it for all owned games each time new game is added
// Quick database setup
// Create all necessary entities, repos and services for CRUD with data and converters
// Create service for exchaning info with steamApi
// Service for logging in to the app (userDetailsService), open old project for quick check
// Create serivce for adding ratings and reviews
// Create service which will output recommendations (one for algorithmic and one for AI)
// HTMX for frontend (docs and learn)
// Think about saving model for NCF
@SpringBootApplication
public class GameRecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameRecommenderApplication.class, args);
    }
}
