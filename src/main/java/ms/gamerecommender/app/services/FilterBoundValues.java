package ms.gamerecommender.app.services;

import lombok.experimental.UtilityClass;

@UtilityClass
class FilterBoundValues {
    double PRICE_LOW_BOUND = 0.0;
    double PRICE_HIGH_BOUND = Double.MAX_VALUE;
    double POSITIVE_REVIEW_PERCENTAGE_LOW_BOUND = 0.0;
    double POSITIVE_REVIEW_PERCENTAGE_HIGH_BOUND = 100.0;
    double METACRITIC_SCORE_LOW_BOUND = 0.0;
    double METACRITIC_SCORE_HIGH_BOUND = 100.0;
    double TIME_PLAYED_LOW_BOUND = 0.0;
    double TIME_PLAYED_HIGH_BOUND = Double.MAX_VALUE;
    double RATING_LOW_BOUND = 0.0;
    double RATING_HIGH_BOUND = 5.0;
    double SCORE_LOW_BOUND = 0.0;
    double SCORE_HIGH_BOUND = 100.0; //TODO: not sure
}
