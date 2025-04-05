package ms.gamerecommender.app.entities.steam;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ReviewSteamResponseDeserializer.class)
public record ReviewSteamResponse(int totalPositiveReviews, int totalNegativeReviews) { }
