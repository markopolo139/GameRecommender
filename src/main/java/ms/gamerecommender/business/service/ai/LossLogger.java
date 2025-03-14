package ms.gamerecommender.business.service.ai;

import ai.djl.training.Trainer;
import ai.djl.training.listener.TrainingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LossLogger implements TrainingListener {

    private static final Logger logger = LoggerFactory.getLogger(LossLogger.class);

    @Override
    public void onEpoch(Trainer trainer) {
        // Log loss at the end of each epoch
        System.out.printf("Train Loss: %s, Validation Loss: %s \n", trainer.getTrainingResult().getTrainLoss(), trainer.getTrainingResult().getValidateLoss());
    }

    @Override
    public void onTrainingBatch(Trainer trainer, BatchData batchData) {}

    @Override
    public void onValidationBatch(Trainer trainer, BatchData batchData) {}

    @Override
    public void onTrainingBegin(Trainer trainer) {
        logger.info("Training begins.");
    }

    @Override
    public void onTrainingEnd(Trainer trainer) {
        logger.info("Training ends.");
    }
}
