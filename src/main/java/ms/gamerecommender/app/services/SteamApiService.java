package ms.gamerecommender.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.app.entities.AppGame;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SteamApiService {
    static String STEAM_API_KEY = "5EB98254243F08E58EAF06842129AE72";
    static String STEAM_REVIEWS_URL = "https://store.steampowered.com/appreviews/";
    static String STEAM_APP_INFO_URL = "http://store.steampowered.com/api/appdetails?appids=";
    static String STEAM_API_BASE_URL = "https://api.steampowered.com/";

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    public AppGame getAppInfo(int appId) {
        String url = STEAM_APP_INFO_URL + appId;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        //TODO: add request for getting reviews

        try {
            return objectMapper.readValue(jsonResponse, AppGame.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Steam API response", e);
        }
    }
}
