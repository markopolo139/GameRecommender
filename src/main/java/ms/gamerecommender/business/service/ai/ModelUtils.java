package ms.gamerecommender.business.service.ai;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserProfile;

import static ms.gamerecommender.business.service.ai.DeepLearningUtilities.createModelDatasetForUser;
import static ms.gamerecommender.business.service.ai.DeepLearningUtilities.transformToFeatureVector;

@UtilityClass
public class ModelUtils {
    private SequentialBlock firstModel(int inputSize) {
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
    private void modelTraining(Model model, ModelDataset dataset, int inputSize, int numberOfEpochs) {
        val config = new DefaultTrainingConfig(Loss.l2Loss())
                .optOptimizer(Optimizer.adam().build())
                .addTrainingListeners(TrainingListener.Defaults.logging());

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.initialize(new Shape(1, inputSize));
            EasyTrain.fit(trainer, numberOfEpochs, dataset, null);
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }

    private float predict(Model model, FeatureVector inputFeature) {
        try (Predictor<float[], Float> predictor = model.newPredictor(new FeatureTranslator())) {
            return predictor.predict(inputFeature.getFeatureArray());
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }

    public float trainAndPredict(UserProfile user, Game newGame, int numberOfEpochs) {
        val dataset = createModelDatasetForUser(user);
        val newGameVector = transformToFeatureVector(newGame);

        try (val model = Model.newInstance("game-recommender")) {
            model.setBlock(ModelUtils.firstModel(FeatureVector.VECTOR_SIZE));

            modelTraining(model, dataset, FeatureVector.VECTOR_SIZE, numberOfEpochs);

            return predict(model, newGameVector);
        }
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
