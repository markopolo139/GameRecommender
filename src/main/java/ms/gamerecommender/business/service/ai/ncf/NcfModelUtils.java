package ms.gamerecommender.business.service.ai.ncf;

import ai.djl.Model;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.ParallelBlock;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static ms.gamerecommender.business.service.ai.ModelUtils.*;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NcfModelUtils {

    int EMBEDDING_SIZE = 32;
    int[] MLP_LAYERS = new int[] {64, 32, 16};

    private SequentialBlock ncfModel(int numberOfUsers, int numberOfGames) {
        val block = new SequentialBlock();

        val parallelBlock = new ParallelBlock(
                list -> {
                    NDList userEmbedding = list.get(0);
                    NDList gameEmbedding = list.get(1);
                    return new NDList(userEmbedding.singletonOrThrow().concat(gameEmbedding.singletonOrThrow(), -1));
                }
        );

        parallelBlock.add(new UserGameEmbedding(numberOfUsers, EMBEDDING_SIZE));
        parallelBlock.add(new UserGameEmbedding(numberOfGames, EMBEDDING_SIZE));

        block.add(parallelBlock);

        block.add(Blocks.batchFlattenBlock());

        for (int layer:  MLP_LAYERS) {
            block.add(Linear.builder().setUnits(layer).build());
            block.add(Activation.reluBlock());
        }

        block.add(Linear.builder().setUnits(1).build());

        return block;
    }

    @SneakyThrows
    public float trainAndPredict(String csvFilePath, int userId, int gameId, int maxUsers, int maxGames, int numberOfEpochs) {
        val dataset = createModelDataset(csvFilePath);

        try (val model = Model.newInstance("game-recommender")) {
            model.setBlock(ncfModel(maxUsers, maxGames));

            modelTraining(model, dataset, new Shape(1), numberOfEpochs);

            val predictor = model.newPredictor(new NcfTranslator());

            return predictor.predict(new NcfInput(userId, gameId));
        }
    }

    @SneakyThrows
    public List<Float> trainAndBatchPredict(String csvFilePath, int userId, List<Integer> gameIds, int maxUsers, int maxGames, int numberOfEpochs) {
        val dataset = createModelDataset(csvFilePath);
        val predictInput = gameIds.stream().map(gameId -> new NcfInput(userId, gameId)).toList();

        try (val model = Model.newInstance("game-recommender")) {
            model.setBlock(ncfModel(maxUsers, maxGames));

            modelTraining(model, dataset, new Shape(1), numberOfEpochs);

            val predictor = model.newPredictor(new NcfTranslator());

            return predictor.batchPredict(predictInput);
        }
    }

    @SneakyThrows
    private List<NcfDataPoint> loadFromCsv(String csvFilePath) {
        val dataPoints = new ArrayList<NcfDataPoint>();

        try (Reader reader = new FileReader(csvFilePath)) {
            CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT);

            for (CSVRecord csvRecord : csvParser) {
                int userId = Integer.parseInt(csvRecord.get(0).trim());
                int gameId = Integer.parseInt(csvRecord.get(1).trim());
                int score = Integer.parseInt(csvRecord.get(2).trim());

                dataPoints.add(new NcfDataPoint(userId, gameId, score));
            }
        }

        return dataPoints;
    }

    private ModelDataset createModelDataset(String csvFilePath) {
        return new ModelDataset(loadFromCsv(csvFilePath));
    }

    private class NcfTranslator implements Translator<NcfInput, Float> {

        @Override
        public Float processOutput(TranslatorContext ctx, NDList list) throws Exception {
            return list.getFirst().getFloat();
        }

        @Override
        public NDList processInput(TranslatorContext ctx, NcfInput input) throws Exception {
            NDManager manager = ctx.getNDManager();
            NDArray userId = manager.create(new int[]{input.userId()});
            NDArray gameId = manager.create(new int[]{input.gameId()});
            return new NDList(userId, gameId);
        }
    }
}
