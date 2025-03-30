package ms.gamerecommender.business.service.ai.ncf;

import lombok.SneakyThrows;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;
import ms.gamerecommender.business.value.UserProfile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static ms.gamerecommender.business.service.ai.SyntheticDataGenerator.*;
import static ms.gamerecommender.business.service.ai.ncf.NcfModelUtils.*;

class NcfModelUtilsTest {
    private final String csvFilePath = "src/test/resources/dataModelForNCF.csv";

    @Test
    void testIfModelIsConfiguredCorrectly() {
        val dataset = createDataset(10000,3);
        val user = createUserProfile(1, dataset,50);

        val users = List.of(
                user,
                createUserProfile(2, dataset,50),
                createUserProfile(3, dataset,50),
                createUserProfile(4, dataset,50),
                createUserProfile(5, dataset,50),
                createUserProfile(6, dataset,50),
                createUserProfile(7, dataset,50),
                createUserProfile(8, dataset,50),
                createUserProfile(9, dataset,50),
                createUserProfile(10, dataset,50),
                createUserProfile(11, dataset,50),
                createUserProfile(12, dataset,50),
                createUserProfile(13, dataset,50),
                createUserProfile(14, dataset,50),
                createUserProfile(15, dataset,50)
        );

        val notPlayedGames = dataset.stream().map(Game::getGameId).filter(
                gameId -> user.ownedGames().stream().allMatch(userGame -> userGame.getGameId() != gameId)
        ).toList();

        val predicitons = trainAndBatchPredict(mapUsersToNcfData(users), user.userId(), notPlayedGames, users.size() + 1, dataset.size(), 15);

        System.out.println();
    }

    @SneakyThrows
    private void createCsvFile(List<UserProfile> users) {
        val csvFile = new File(csvFilePath);
        csvFile.createNewFile();

        try (FileWriter fileWriter = new FileWriter(csvFile); CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
            for (UserProfile user : users) {
                for (UserGame game: user.ownedGames()) {
                    csvPrinter.printRecord(user.userId(), game.getGameId(), game.calculateScore());
                }
            }
        }
    }

    private List<NcfDataPoint> mapUsersToNcfData(List<UserProfile> users) {
        List<NcfDataPoint> ncfDataPoints = new ArrayList<>();
        for (UserProfile user : users) {
            for (UserGame game: user.ownedGames()) {
                ncfDataPoints.add(new NcfDataPoint(user.userId(), game.getGameId(), (float) game.calculateScore()));
            }
        }

        return ncfDataPoints;
    }
}