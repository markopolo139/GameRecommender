package ms.gamerecommender.app.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_profiles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    int id;

    @Column(name = "username", nullable = false, unique = true)
    @Setter
    String username;

    @Column(name = "password", nullable = false)
    @Setter
    String password;

    @OneToMany(mappedBy = "userProfileEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<UserGameEntity> userGames;

    public UserProfileEntity(String username, String password) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.userGames = new HashSet<>();
    }

    protected UserProfileEntity() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileEntity that = (UserProfileEntity) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }
}
