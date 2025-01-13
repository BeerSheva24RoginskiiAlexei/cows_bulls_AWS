package telran.game.db;

import java.time.LocalDate;

import telran.game.entities.Game;
import telran.game.exceptions.GameNotFoundException;

public class BullCowServiceImpl implements BullCowService {
    private final BullsCowsRepository repository;

    public BullCowServiceImpl(BullsCowsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createGame(String sequence) {
        repository.createGame(sequence);
    }

    @Override
    public void joinToGame(String username, long gameId) {
        repository.joinToGame(username, gameId);
    }

    @Override
    public void startGame(String username, long gameId) {
        repository.startGame(username, gameId);
    }

    @Override
    public void makeMove(String username, long gameId, String sequence) {
        int bulls = calculateBulls(sequence, gameId);
        int cows = calculateCows(sequence, gameId);
        repository.createMove(username, gameId, sequence, bulls, cows);
    }

    @Override
    public void finishGame(long gameId) {
        repository.setGameIsFinished(gameId);
    }

    private Game getGame(long gameId) {
        Game game = repository.findGameById(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    private int calculateBulls(String sequence, long gameId) {
        Game game = getGame(gameId);
        String trueSequence = game.getSequence();

        int bulls = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == trueSequence.charAt(i)) {
                bulls++;
            }
        }

        return bulls;
    }

    private int calculateCows(String sequence, long gameId) {
        Game game = getGame(gameId);
        String trueSequence = game.getSequence();

        int cows = 0;

        boolean[] usedTrueSequence = new boolean[sequence.length()];
        boolean[] usedUserSequence = new boolean[sequence.length()];

        // Перебираем и ставим пометки для быков
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == trueSequence.charAt(i)) {
                usedTrueSequence[i] = true;
                usedUserSequence[i] = true;
            }
        }

        // Перебираем для коров
        for (int i = 0; i < sequence.length(); i++) {
            if (usedUserSequence[i]) {
                continue;
            }
            for (int j = 0; j < trueSequence.length(); j++) {
                if (!usedTrueSequence[j] && sequence.charAt(i) == trueSequence.charAt(j)) {
                    cows++;
                    usedTrueSequence[j] = true;
                    break;
                }
            }
        }

        return cows;
    }

    @Override
    public void registerUser(String username, LocalDate dateOfBirth) {
        // Call the repository to create the user
        long userId = repository.createUser(username, dateOfBirth);
        System.out.println("User registered with ID: " + userId);
    }

    @Override
    public void createMove(String username, long gameId, String sequence, int bulls, int cows) {
        repository.createMove(username, gameId, sequence, bulls, cows);
        System.out.println("Move created for game " + gameId + " by " + username);
    }

    @Override
    public String getGameStatus(long gameId) {
        // Fetch the game from the repository
        Game game = repository.findGameById(gameId);
        return "Game ID: " + game.getId() + ", Started: " + game.isStarted() + ", Finished: " + game.isFinished();
    }
}
