package telran.game.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String sequence;

    @Column(name = "is_started", nullable = false)
    private boolean isStarted = false;

    @Column(name = "is_finished", nullable = false)
    private boolean isFinished = false;

    public long getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setGameIsFinished() {
        this.isFinished = true;
    }

    @Override
    public String toString() {
        return "Game [id=" + id + ", sequence=" + sequence + ", isStarted=" + isStarted + ", isFinished=" + isFinished
                + "]";
    }
}
