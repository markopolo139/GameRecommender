package ms.gamerecommender.app.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ms.gamerecommender.business.value.UserGame;

import java.util.Objects;

@Entity
@Table(name = "user_games")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class UserGameEntity {

    @EmbeddedId
    UserGameId userGameId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserProfileEntity userProfileEntity;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    GameEntity gameEntity;

    @Column(name = "time_played", nullable = false)
    float timePlayed;

    @Column(name = "rating", nullable = false)
    @Setter
    float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "review", nullable = false)
    @Setter
    UserGame.Review review;

    @Column(name = "score", nullable = false)
    @Setter
    float score;

    protected UserGameEntity() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserGameEntity that = (UserGameEntity) o;
        return Objects.equals(getUserGameId(), that.getUserGameId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserGameId());
    }
}
