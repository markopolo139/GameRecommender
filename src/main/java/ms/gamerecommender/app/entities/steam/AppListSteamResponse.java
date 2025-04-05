package ms.gamerecommender.app.entities.steam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = AppListSteamResponse.AppListSteamResponseDeserializer.class)
public record AppListSteamResponse(List<Integer> appId) {
    static class AppListSteamResponseDeserializer extends StdDeserializer<AppListSteamResponse> {
        protected AppListSteamResponseDeserializer() {
            this(null);
        }

        protected AppListSteamResponseDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public AppListSteamResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            List<Integer> appIds = new ArrayList<>();

            JsonNode rootNode = p.getCodec().readTree(p);
            JsonNode responseNode = rootNode.path("applist");
            JsonNode appsNode = responseNode.path("apps");

            for (JsonNode app : appsNode) {
                appIds.add(app.get("appid").asInt());
            }

            return new AppListSteamResponse(appIds);
        }
    }
}
