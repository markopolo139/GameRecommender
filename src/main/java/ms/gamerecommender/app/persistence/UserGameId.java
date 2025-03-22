package ms.gamerecommender.app.persistence;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserGameId {
    int userId;
    int gameId;

    protected UserGameId() {}
}
