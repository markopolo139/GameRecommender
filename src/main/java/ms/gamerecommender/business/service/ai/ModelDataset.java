package ms.gamerecommender.business.service.ai;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelDataset extends RandomAccessDataset {
    List<FeatureVector> features;

    public ModelDataset(List<FeatureVector> features) {
        super(new Builder());
        this.features = features;
    }

    @Override
    public Record get(NDManager manager, long index) throws IOException {
        FeatureVector featureVector = features.get((int) index);
        float[] features = featureVector.getFeatureArray();
        float targetScore = (float) featureVector.getTargetScore();

        NDArray data = manager.create(features);
        NDArray label = manager.create(new float[]{targetScore});

        return new Record(new NDList(data), new NDList(label));
    }

    @Override
    protected long availableSize() {
        return features.size();
    }

    @Override
    public void prepare(Progress progress) throws IOException, TranslateException {}

    public static class Builder extends RandomAccessDataset.BaseBuilder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
