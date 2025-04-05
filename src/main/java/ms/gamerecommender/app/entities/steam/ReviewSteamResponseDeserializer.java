package ms.gamerecommender.app.entities.steam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ReviewSteamResponseDeserializer extends StdDeserializer<ReviewSteamResponse> {
    protected ReviewSteamResponseDeserializer() {
        this(null);
    }

    protected ReviewSteamResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ReviewSteamResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode rootNode = p.getCodec().readTree(p);
        JsonNode queryNode = rootNode.path("query_summary");

        int positiveReviewNumber = queryNode.path("total_positive").asInt();
        int negativeReviewNumber = queryNode.path("total_negative").asInt();

        return new ReviewSteamResponse(positiveReviewNumber, negativeReviewNumber);
    }
}
