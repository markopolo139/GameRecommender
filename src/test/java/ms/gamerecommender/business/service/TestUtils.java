package ms.gamerecommender.business.service;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.model.Preference;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TestUtils {
    @SneakyThrows
    public List<Preference> readCsvFile(String csvPath) {
        val csvFile = new File(csvPath);
        List<Preference> preferences = new ArrayList<>();
        try (FileReader fileReader = new FileReader(csvFile); CSVParser csvParser = CSVParser.parse(fileReader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord:  csvParser) {
                preferences.add(new GenericPreference(
                        Long.parseLong(csvRecord.get(0)), Long.parseLong(csvRecord.get(1)), Float.parseFloat(csvRecord.get(2)))
                );
            }
        }

        return preferences;
    }
}
