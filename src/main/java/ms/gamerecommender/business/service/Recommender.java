package ms.gamerecommender.business.service;

import java.util.List;

public interface Recommender<IN, OUT> {
    List<OUT> recommend(IN input, List<OUT> dataset, int topN);
}
