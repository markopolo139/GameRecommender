package ms.gamerecommender.business.value;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter
public class Game {
    int gameId;
    String title;
    Set<String> tags;

    @Setter
    @NonFinal
    double positiveReviewPercentage;
}
