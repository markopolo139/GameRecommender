package ms.gamerecommender.business.service;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecommenderUtils {
    public double POSITIVE_REVIEW_PERCENTAGE_CUT_OFF = 80;
    public double TOP_N_FACTOR = 0.75;
    String TAG_FILE_PATH = "src/main/resources/dataModel.csv";

    @SneakyThrows
    public List<String> readTagsFromFile() {
        List<String> tags = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TAG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tags.add(line.trim());
            }
        }

        return tags;
    }
}
