package ms.gamerecommender.business.service;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ms.gamerecommender.business.DatasetUtility.*;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
class ContentBasedGameRecommender {

    double positiveReviewPercentageCutOff = 80;

    private Map<String, Double> getTagsPreferenceMap(List<UserGame> playedGames) {
        Map<String, Double> tagsPreferenceMap = Maps.newHashMap();
        playedGames.forEach(game -> {
            game.getTags().forEach(tag -> {
                tagsPreferenceMap.put(tag, tagsPreferenceMap.getOrDefault(tag, 0.0) + game.calculateScore());
            });
        });

        normalizeMap(tagsPreferenceMap);

        return tagsPreferenceMap;
    }

    private void normalizeMap(Map<String, Double> map) {
        val maxValue = map.values().stream().max(Double::compare).orElse(Double.MAX_VALUE);
        val minValue = map.values().stream().min(Double::compare).orElse(Double.MIN_VALUE);

        if (maxValue.equals(minValue)) {
            map.replaceAll((k, v) -> 0.0);
        }

        map.replaceAll((k, v) -> (v - minValue) / (maxValue - minValue));
    }

    private Map.Entry<Game, Double> calculateScore(Game game, Map<String, Double> tagsPreferenceMap) {
        val tagsScore = game.getTags().stream().map(it -> tagsPreferenceMap.getOrDefault(it, 0.0)).reduce(Double::sum).orElse(0.0);
        return Maps.immutableEntry(game, tagsScore);
    }

    List<Game> recommend(List<UserGame> input, List<Game> dataset, int topN) {
        val tagsPreferenceMap = getTagsPreferenceMap(input);

        Map<Game, Double> similarityScores = removeOwnedGamesFromDatabase(dataset, input).stream()
                .map((game) -> calculateScore(game, tagsPreferenceMap))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return similarityScores.entrySet().stream()
                .filter(entry -> entry.getKey().getPositiveReviewPercentage() >= positiveReviewPercentageCutOff)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }
}
