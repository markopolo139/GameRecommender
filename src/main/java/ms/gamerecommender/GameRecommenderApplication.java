package ms.gamerecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO:
// UserGameService: Add game from database to the user (create UserGame), get Entity from database and map entity to app version for score calculation,
// ^ changing/adding UserGame fields (rating and review), here will be using UserGameEntity directly, no mapping to app version, also will be need to recalculate score
// ^ remove game from list (new function in repo, propably or filter userGames),
// ^ get user info (get owned games), filtered or ordered by (using Specification class)
// GameService: list all possible games, filtered or ordered by
// check SecurityContextHolder
// Think about reloading userGame data, like timePlayed, providing again steamApi, but should I delete all or just update beforhand, some kind of merge function not to lose ratings and review, create new Entities and if already present in ownedGames copy reivew and rating
// ^ function in userGame service for reloading of this data, as prive and later during stream api implementation add public call with steam key to get this info and merge sets
// Code for merging, maybe function merge in UserGame class
//Map<String, UserGame> oldGamesMap = oldSet.stream()
//        .collect(Collectors.toMap(UserGame::getUserId, game -> game));
//
//    newSet.forEach(newGame -> {
//UserGame oldGame = oldGamesMap.get(newGame.getUserId());
//        if (oldGame != null) {
//        // Update timePlayed from new game
//        oldGame.setTimePlayed(newGame.getTimePlayed());
//        // userRating remains unchanged
//        }
//        // If no old game exists, the new one will remain as is
//        });

//TODO:
// Create service which will output recommendations (one for algorithmic and one for AI)
// ^ For dataModel propably make that it will for each recommend call extract from database columns needed via JPA Criterion (check chatGPT) and save it to csv file
// Maybe extract info from database not from csv file, check DeepSeak and maybe search for solution, maybe GenericDataModel can be used with reading of preference List from result of query from above (Criterion)
// ^ For this HybridGameRecommender and NcfModelPredict would need to receive List<Preferences> of ready DAtaModel instead of CSV Path
// Create service for exchaning info with steamApi (RestClient [I think, to check])
// ^ models for outputs and inputs from/to api and converters to Persistence entity
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