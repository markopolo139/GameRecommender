package ms.gamerecommender.business.service;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import lombok.val;
import ms.gamerecommender.business.value.Game;
import ms.gamerecommender.business.value.UserGame;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
class ContentBasedGameRecommender {

    private Map<String, Double> getGenrePreferenceMap(List<UserGame> playedGames) {
        Map<String, Double> genrePreferenceMap = Maps.newHashMap();
        playedGames.forEach(game -> {
            game.getGenres().forEach(genre -> {
                genrePreferenceMap.put(genre, genrePreferenceMap.getOrDefault(genre, 0.0) + game.calculateScore());
            });
        });

        normalizeMap(genrePreferenceMap);

        return genrePreferenceMap;
    }

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

    private Map.Entry<Game, Double> calculateScore(Game game, Map<String, Double>  genrePreferenceMap, Map<String, Double> tagsPreferenceMap) {
        val genreScore = game.getGenres().stream().map(it -> genrePreferenceMap.getOrDefault(it, 0.0)).reduce(Double::sum).orElse(0.0);
        val tagsScore = game.getTags().stream().map(it -> tagsPreferenceMap.getOrDefault(it, 0.0)).reduce(Double::sum).orElse(0.0);
        return Maps.immutableEntry(game, genreScore + tagsScore);
    }

    private boolean checkIfGamePresentInOwnedGames(Game game, List<UserGame> playedGames) {
        return playedGames.stream().map(Game::getGameId).noneMatch(gameId -> gameId.equals(game.getGameId()));
    }

    List<Game> recommend(List<UserGame> input, List<Game> dataset, int topN) {
        val genresPreferenceMap = getGenrePreferenceMap(input);
        val tagsPreferenceMap = getTagsPreferenceMap(input);

        Map<Game, Double> similarityScores = dataset.stream().filter((game -> checkIfGamePresentInOwnedGames(game, input)))
                .map((game) -> calculateScore(game, genresPreferenceMap, tagsPreferenceMap))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }
}
