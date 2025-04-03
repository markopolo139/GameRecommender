package ms.gamerecommender.app.entities.steam;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ms.gamerecommender.app.entities.AppGame;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ms.gamerecommender.business.service.RecommenderUtils.*;

public class SteamAppInfoDeserializer extends StdDeserializer<AppGame> {
    static List<String> AVAILABLE_TAGS = readTagsFromFile();

    public SteamAppInfoDeserializer() {
        this(null);
    }

    protected SteamAppInfoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AppGame deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode rootNode = p.getCodec().readTree(p);

        JsonNode appEntry = rootNode.fields().next().getValue();
        JsonNode dataNode = appEntry.path("data");

        int appId = dataNode.path("steam_appid").asInt();
        String title = dataNode.path("name").asText();

        Double metacriticScore = dataNode.path("metacritic").path("score").isMissingNode()
                ? null
                : dataNode.path("metacritic").path("score").asDouble();

        Double price = dataNode.path("price_overview").isMissingNode()
                ? null
                : dataNode.path("price_overview").path("initial").asDouble() / 100;

        Set<String> tags = new HashSet<>();

        JsonNode categories = dataNode.path("categories");
        for (JsonNode category : categories) {
            tags.add(category.path("description").asText());
        }

        JsonNode genres = dataNode.path("genres");
        for (JsonNode genre : genres) {
            tags.add(genre.path("description").asText());
        }

        tags = tags.stream().filter(it -> AVAILABLE_TAGS.contains(it)).collect(Collectors.toSet());

        return new AppGame(
                appId,
                title,
                tags,
                -1,
                metacriticScore,
                price
        );
    }
}
