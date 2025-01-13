package telran.game.entities;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "gamer")
public class Gamer {
    @Id
    private String username;
    @Column(nullable = false)
    private LocalDate birthdate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Gamer [username=" + username + ", birthdate=" + birthdate + "]";
    }
}
