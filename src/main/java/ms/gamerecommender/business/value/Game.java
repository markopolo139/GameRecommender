package ms.gamerecommender.business.value;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter
public class Game {
    int gameId;
    String title;
    Set<String> genres;
    Set<String> tags;
}
