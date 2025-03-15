package ms.gamerecommender.business.service.ai.dl;

import ai.djl.Model;
import ai.djl.ndarray.NDList;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;

import java.util.List;

import static ms.gamerecommender.business.service.ai.ModelUtils.*;

@UtilityClass
public class DlModelUtils {
    private SequentialBlock simpleDlModel(int inputSize) {
        val block = new SequentialBlock();
        block.add(Blocks.batchFlattenBlock(inputSize));
        block.add(Linear.builder().setUnits(128).build());
        block.add(Activation.reluBlock());
        block.add(Linear.builder().setUnits(64).build());
        block.add(Activation.reluBlock());
        block.add(Linear.builder().setUnits(1).build());
        return block;
    }

    @SneakyThrows
    public float trainAndPredict(UserProfile user, Game newGame, int numberOfEpochs) {
        val dataset = createModelDatasetForUser(user);
        val newGameVector = transformToFeatureVector(newGame);

        try (val model = Model.newInstance("game-recommender")) {
            model.setBlock(DlModelUtils.simpleDlModel(FeatureVector.VECTOR_SIZE));

            modelTraining(model, dataset, FeatureVector.VECTOR_SIZE, numberOfEpochs);

            val predictor = model.newPredictor(new FeatureTranslator());

            return predictor.predict(newGameVector.getFeatureArray());
        }
    }

    @SneakyThrows
    public List<Float> trainAndBatchPredict(UserProfile user, List<Game> newGames, int numberOfEpochs) {
        val dataset = createModelDatasetForUser(user);
        val newGameVector = transformToFeatureVectorList(newGames);

        try (val model = Model.newInstance("game-recommender")) {
            model.setBlock(DlModelUtils.simpleDlModel(FeatureVector.VECTOR_SIZE));

            modelTraining(model, dataset, FeatureVector.VECTOR_SIZE, numberOfEpochs);

            val predictor = model.newPredictor(new FeatureTranslator());

            return predictor.batchPredict(newGameVector.stream().map(FeatureVector::getFeatureArray).toList());
        }
    }

    public List<FeatureVector> createFeatureVectorListForUser(UserProfile userProfile) {
        return userProfile.ownedGames().stream().map(FeatureVector::new).toList();
    }

    public ModelDataset createModelDatasetForUser(UserProfile userProfile) {
        return new ModelDataset(createFeatureVectorListForUser(userProfile));
    }

    public FeatureVector transformToFeatureVector(Game game) {
        return new FeatureVector(game);
    }

    public List<FeatureVector> transformToFeatureVectorList(List<Game> games) {
        return games.stream().map(DlModelUtils::transformToFeatureVector).toList();
    }

    private class FeatureTranslator implements Translator<float[], Float> {

        @Override
        public Float processOutput(TranslatorContext ctx, NDList list) throws Exception {
            return list.getFirst().getFloat();
        }

        @Override
        public NDList processInput(TranslatorContext ctx, float[] input) throws Exception {
            return new NDList(ctx.getNDManager().create(input));
        }
    }
}
