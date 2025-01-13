package telran.game.db;

import java.time.LocalDate;

public interface BullCowService {

    void createGame(String sequence);

    void joinToGame(String username, long gameId);

    void startGame(String username, long gameId);

    void makeMove(String username, long gameId, String sequence);  

    void finishGame(long gameId);  

    void registerUser(String username, LocalDate dateOfBirth);

    void createMove(String username, long gameId, String sequence, int bulls, int cows);

    String getGameStatus(long gameId);
}
