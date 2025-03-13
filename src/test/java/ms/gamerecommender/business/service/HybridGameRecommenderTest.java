package ms.gamerecommender.business.service;

import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static ms.gamerecommender.business.service.ValueFactory.*;

class HybridGameRecommenderTest {

    private String csvPath =  "src/test/resources/dataModel.csv";
    private final Recommender<UserProfile, Game> recommender = new HybridGameRecommender(6, csvPath);

    @AfterEach
    void tearDown() {
        resetGameId();
    }

    @Test
    void generalRecommendationTest() throws IOException {
        val dataset = List.of(
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre3"), Set.of("tag3")),
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre3"), Set.of("tag1")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre3"), Set.of("tag2")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre1"), Set.of("tag1")),
                createGame(Set.of("Genre2"), Set.of("tag2")),
                createGame(Set.of("Genre1"), Set.of("tag2"))
        );

        val user = createUserProfile(List.of(
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        ));

        val recommendations = recommender.recommend(user, dataset, 10);

        Assertions.assertNotNull(recommendations);
        Assertions.assertFalse(recommendations.isEmpty());
        Assertions.assertEquals(10, recommendations.size());
        Assertions.assertTrue(dataset.containsAll(recommendations));

    }
}