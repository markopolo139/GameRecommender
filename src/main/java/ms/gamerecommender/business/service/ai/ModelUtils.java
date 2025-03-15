package ms.gamerecommender.business.service.ai;

import ai.djl.Model;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.translate.TranslateException;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelUtils {
    @SneakyThrows
    public <D extends RandomAccessDataset> void modelTraining(Model model, D dataset, Shape inputShape, int numberOfEpochs) {
        val datasetSeparated = dataset.randomSplit(8, 2);

        val config = new DefaultTrainingConfig(Loss.l2Loss())
                .optOptimizer(Optimizer.adam().build())
                .addTrainingListeners(TrainingListener.Defaults.logging())
                .addTrainingListeners(new LossLogger());

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.initialize(inputShape);
            EasyTrain.fit(trainer, numberOfEpochs, datasetSeparated[0], datasetSeparated[1]);
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }
}
