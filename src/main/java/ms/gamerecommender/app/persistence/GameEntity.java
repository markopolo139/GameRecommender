package ms.gamerecommender.app.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "games")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class GameEntity {

    @Id
    @Column(name = "game_id", nullable = false)
    int id;

    @Column(name = "title", nullable = false)
    String title;

    @ElementCollection
    @CollectionTable(name = "game_tags", joinColumns = {@JoinColumn(name = "game_id")})
    @Column(name = "tag", nullable = false)
    Set<String> tags;

    @Column(name = "positive_review_percentage", nullable = false)
    double positiveReviewPercentage;

    @Nullable
    @Column(name = "metacritic_score", nullable = false)
    Double metacriticScore;

    @Nullable
    @Column(name = "price", nullable = false)
    Double price;

    protected GameEntity() {}

    @PrePersist
    private void validate() {
        if (metacriticScore == null) {
            metacriticScore = 0.0;
        }

        if(price == null) {
            price = 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
