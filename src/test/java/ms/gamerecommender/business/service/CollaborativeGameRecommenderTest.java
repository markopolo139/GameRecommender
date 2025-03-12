package ms.gamerecommender.business.service;

import lombok.val;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static ms.gamerecommender.business.service.ValueFactory.*;

class CollaborativeGameRecommenderTest {

    private String csvPathForOneUser =  "src/test/resources/dataModelOneUser.csv";
    private String csvPath =  "src/test/resources/dataModel.csv";

    @AfterEach
    void tearDown() {
        resetGameId();
    }

    @Test
    void collaborativeUserRecommendationTest() throws IOException {
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
                createGame(Set.of("Genre2"), Set.of("tag2"))
        );

        val collaborativeRecommendations = CollaborativeGameRecommender.recommend(
                1, new FileDataModel(new File(csvPathForOneUser)), dataset, 3, 3
        );

        Assertions.assertNotNull(collaborativeRecommendations);
        Assertions.assertFalse(collaborativeRecommendations.isEmpty());
        Assertions.assertEquals(dataset.get(7), collaborativeRecommendations.getFirst());
    }
}