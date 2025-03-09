package ms.gamerecommender.business.service;

import lombok.val;
import ms.gamerecommender.business.value.UserGame;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static ms.gamerecommender.business.service.ValueFactory.*;

class ContentBasedGameRecommenderTest {

    @AfterEach
    void tearDown() {
        resetGameId();
    }

    @Test
    void userPlayedAllGames() {
        val dataset = List.of(
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre3"), emptySet())
        );

        val userGames = List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        );

        val recommendations = ContentBasedGameRecommender.recommend(userGames, dataset, 10);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    void recommendationByOneGenreTest() {
        val dataset = List.of(
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre3"), emptySet()),
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre1"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre2"), emptySet()),
                createGame(Set.of("Genre3"), emptySet()),
                createGame(Set.of("Genre3"), emptySet())
        );

        val userGames = List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        );

        val top3 = ContentBasedGameRecommender.recommend(userGames, dataset, 3);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 8), top3));

        val top5 = ContentBasedGameRecommender.recommend(userGames, dataset, 5);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 10), top5));
    }

    @Test
    void recommendationByOneTagTest() {
        val dataset = List.of(
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag3")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag3")),
                createGame(emptySet(), Set.of("tag3"))
        );

        val userGames = List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        );

        val top3 = ContentBasedGameRecommender.recommend(userGames, dataset, 3);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 8), top3));

        val top5 = ContentBasedGameRecommender.recommend(userGames, dataset, 5);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 10), top5));
    }

    @Test
    void recommendationByMultiplyTagTest() {
        val dataset = List.of(
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag3")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag1")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag2")),
                createGame(emptySet(), Set.of("tag3")),
                createGame(emptySet(), Set.of("tag3"))
        );

        val userGames = List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        );

        val top3 = ContentBasedGameRecommender.recommend(userGames, dataset, 3);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 8), top3));

        val top5 = ContentBasedGameRecommender.recommend(userGames, dataset, 5);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 10), top5));
    }

    @Test
    void recommendationTest() {
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
                createGame(Set.of("Genre3"), Set.of("tag3")),
                createGame(Set.of("Genre3"), Set.of("tag3"))
        );

        val userGames = List.of(
                createUserGame(dataset.getFirst(), 500, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(1), 250, 5, UserGame.Review.POSITIVE),
                createUserGame(dataset.get(2), 100, 4, UserGame.Review.NONE),
                createUserGame(dataset.get(3), 30, 3, UserGame.Review.NONE),
                createUserGame(dataset.get(4), 1, 1, UserGame.Review.NEGATIVE)
        );

        val top3 = ContentBasedGameRecommender.recommend(userGames, dataset, 3);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 8), top3));

        val top5 = ContentBasedGameRecommender.recommend(userGames, dataset, 5);
        Assertions.assertTrue(CollectionUtils.isEqualCollection(dataset.subList(5, 10), top5));
    }

}