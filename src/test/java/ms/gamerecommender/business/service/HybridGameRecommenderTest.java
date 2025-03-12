package ms.gamerecommender.business.service;

import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static ms.gamerecommender.business.service.ValueFactory.resetGameId;

class HybridGameRecommenderTest {

    private String csvPath =  "src/test/resources/dataModel.csv";
    private final Recommender<UserProfile, Game> recommender = new HybridGameRecommender(csvPath);

    @AfterEach
    void tearDown() {
        resetGameId();
    }

    @Test
    void generalRecommendationTest() {

    }
}