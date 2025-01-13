package telran.game;

import telran.game.db.jpa.entities.Game;
import telran.game.db.jpa.entities.Gamer;

public class MoveResult {
    private String sequence;
    private int bulls;
    private int cows;
    private Gamer gamer;
    private Game game;

    public MoveResult(String sequence, int bulls, int cows, Gamer gamer, Game game) {
        this.sequence = sequence;
        this.bulls = bulls;
        this.cows = cows;
        this.gamer = gamer;
        this.game = game;
    }

    public String getSequence() {
        return sequence;
    }

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }

    public Gamer getGamer() {
        return gamer;
    }

    public Game getGame() {
        return game;
    }

    // Сеттеры
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setBulls(int bulls) {
        this.bulls = bulls;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }

    public void setGamer(Gamer gamer) {
        this.gamer = gamer;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "MoveResult [sequence=" + sequence + ", bulls=" + bulls + ", cows=" + cows
                + ", gamer=" + gamer.getUsername() + ", game=" + game.getId() + "]";
    }
}
