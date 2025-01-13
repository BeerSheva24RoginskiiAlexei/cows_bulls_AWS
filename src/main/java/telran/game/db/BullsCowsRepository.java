package telran.game.db;

import java.time.LocalDate;
import java.util.List;

import telran.game.MoveResult;
import telran.game.entities.Game;

public interface BullsCowsRepository {
    public boolean isGamerExists(String username);

    public long createUser(String username, LocalDate dateOfBirth);

    public long createGame(String sequence);

    public List<Long> findJoinebleGames(String username);

    public void joinToGame(String username, long gameId);

    public List<Long> findStartebleGames(String username);

    public void startGame(String username, long gameId);

    public void createMove(String username, long gameId, String sequence, int bulls, int cows);

    public String findWinnerGame(long gameId);

    public void setWinnerGame(String username, long gameId);

    public void setGameIsFinished(long gameId);

    public List<MoveResult> findAllMovesGameGamer(String username, long gameId);

    public Game findGameById(long gameId);
}
