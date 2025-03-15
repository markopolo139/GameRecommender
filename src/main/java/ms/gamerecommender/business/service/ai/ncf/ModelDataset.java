package ms.gamerecommender.business.service.ai.ncf;

import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.BatchSampler;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.RandomSampler;
import ai.djl.training.dataset.Record;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.io.IOException;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelDataset extends RandomAccessDataset {
    List<NcfDataPoint> dataPoints;

    public ModelDataset(List<NcfDataPoint> dataPoints) {
        super(new Builder().setSampling(new BatchSampler(new RandomSampler(), 16)));
        this.dataPoints = dataPoints;
    }

    @Override
    public Record get(NDManager manager, long index) throws IOException {
        val point =  dataPoints.get((int) index);

        val userId = manager.create(point.userId());
        val gameId = manager.create(point.gameId());
        val targetScore = manager.create(point.score());

        return new Record(new NDList(userId, gameId), new NDList(targetScore));
    }

    @Override
    protected long availableSize() {
        return dataPoints.size();
    }

    @Override
    public void prepare(Progress progress) throws IOException, TranslateException { }

    public static class Builder extends RandomAccessDataset.BaseBuilder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
