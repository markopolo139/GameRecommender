package ms.gamerecommender.business.value;

import java.util.List;

public record UserProfile(int userId, List<UserGame> ownedGames) { }
