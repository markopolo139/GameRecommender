package ms.gamerecommender.app.config;

import ms.gamerecommender.business.service.HybridGameRecommender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BusinessConfiguration {
    @Bean
    @Scope("singleton")
    public HybridGameRecommender hybridGameRecommender(
            @Value("${recommender.algorithm.csvPath}") String csvPath,
            @Value("${recommender.algorithm.nearest.neighbours}") int nearestNeighbours
    ) {
        return new HybridGameRecommender(nearestNeighbours, csvPath );
    }
}
