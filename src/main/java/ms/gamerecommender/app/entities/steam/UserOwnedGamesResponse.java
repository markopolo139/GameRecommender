package ms.gamerecommender.app.entities.steam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using = UserOwnedGamesResponse.UserOwnedGamesResponseDeserializer.class)
public record UserOwnedGamesResponse(Set<UserOwnedGameResponse> userOwnedGamesResponse) {
    public record UserOwnedGameResponse(int appid, double playedTime) { }

    static class UserOwnedGamesResponseDeserializer extends StdDeserializer<UserOwnedGamesResponse> {
        protected UserOwnedGamesResponseDeserializer() {
            this(null);
        }

        protected UserOwnedGamesResponseDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public UserOwnedGamesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            Set<UserOwnedGameResponse> userOwnedGamesResponse = new HashSet<>();

            JsonNode rootNode = p.getCodec().readTree(p);
            JsonNode responseNode = rootNode.path("response");
            JsonNode gamesNode = responseNode.path("games");

            for (JsonNode game : gamesNode) {
                userOwnedGamesResponse.add(
                        new UserOwnedGameResponse(game.get("appid").asInt(), (double) game.get("playtime_forever").asInt() / 60)
                );
            }

            return new UserOwnedGamesResponse(userOwnedGamesResponse);
        }
    }
}
