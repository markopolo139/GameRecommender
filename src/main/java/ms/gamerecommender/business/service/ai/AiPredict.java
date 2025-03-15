package ms.gamerecommender.business.service.ai;

import java.util.List;
import java.util.stream.Collectors;

public interface AiPredict<IN, OUT> {
    OUT predict(IN input);

    default List<OUT> batchPredict(List<IN> inputList) {
        return inputList.stream().map(this::predict).collect(Collectors.toList());
    };
}
