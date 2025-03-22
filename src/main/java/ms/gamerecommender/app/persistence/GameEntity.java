package ms.gamerecommender.app.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "games")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "metacritic_score")
    double metacriticScore;

    @Column(name = "price")
    double price;

    protected GameEntity() {}

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
