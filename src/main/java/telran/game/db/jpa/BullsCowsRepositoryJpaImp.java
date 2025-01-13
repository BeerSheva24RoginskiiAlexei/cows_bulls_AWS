package telran.game.db.jpa;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import telran.game.MoveResult;
import telran.game.db.BullsCowsRepository;
import telran.game.db.jpa.entities.Game;
import telran.game.db.jpa.entities.GameGamer;
import telran.game.db.jpa.entities.Gamer;
import telran.game.exceptions.GameNotFoundException;
import telran.game.exceptions.GamerNotFoundException;

public class BullsCowsRepositoryJpaImp implements BullsCowsRepository {
    EntityManager em;

    public BullsCowsRepositoryJpaImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public void joinToGame(String username, long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            Gamer gamer = getGamer(username);
            GameGamer gameGamer = new GameGamer(game, gamer);
            em.persist(gameGamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

    }

    @Override
    public boolean isGamerExists(String username) {
        Gamer gamer = em.find(Gamer.class, username);
        return gamer != null;
    }

    @Override
    public long createUser(String username, LocalDate dateOfBirth) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            if (isGamerExists(username)) {
                throw new IllegalArgumentException("User with username '" + username + "' already exists");
            }

            Gamer gamer = new Gamer();
            gamer.setUsername(username);
            gamer.setBirthdate(dateOfBirth);

            em.persist(gamer);
            transaction.commit();

            return username.hashCode();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public long createGame(String sequence) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = new Game();
            game.setSequence(sequence);
            em.persist(game);
            transaction.commit();
            return game.getId();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Long> findJoinebleGames(String username) {
        return em.createQuery(
                "SELECT g.id FROM Game g WHERE g.isFinished = false AND g.id NOT IN " +
                        "(SELECT gg.game.id FROM GameGamer gg WHERE gg.gamer.username = :username)",
                Long.class)
                .setParameter("username", username)
                .getResultList();
    }

    private Gamer getGamer(String username) {
        Gamer gamer = em.find(Gamer.class, username);
        if (gamer == null) {
            throw new GamerNotFoundException(username);
        }
        return gamer;
    }

    private Game getGame(long gameId) {
        Game game = em.find(Game.class, gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    @Override
    public List<Long> findStartebleGames(String username) {
        return em.createQuery(
                "SELECT g.id FROM Game g JOIN GameGamer gg ON g.id = gg.game.id " +
                        "WHERE g.isFinished = false AND gg.gamer.username = :username AND g.isStarted = false",
                Long.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public void startGame(String username, long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            if (game.isStarted()) {
                throw new IllegalStateException("Game is already started");
            }
            game.setStarted(true);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void createMove(String username, long gameId, String sequence, int bulls, int cows) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);

            if (!game.isStarted()) {
                throw new IllegalStateException("Game has not started yet.");
            }
            if (game.isFinished()) {
                throw new IllegalStateException("Game is already finished.");
            }

            Gamer gamer = getGamer(username);
            GameGamer gameGamer = em.createQuery(
                    "SELECT gg FROM GameGamer gg WHERE gg.game.id = :gameId AND gg.gamer.username = :username",
                    GameGamer.class)
                    .setParameter("gameId", gameId)
                    .setParameter("username", username)
                    .getSingleResult();

            MoveResult moveResult = new MoveResult(sequence, bulls, cows, gamer, game);
            em.persist(moveResult);

            if (bulls == 4) {
                setWinnerGame(username, gameId);
                setGameIsFinished(gameId);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public String findWinnerGame(long gameId) {
        Game game = getGame(gameId);
        return em.createQuery(
                "SELECT gg.gamer.username FROM GameGamer gg WHERE gg.game.id = :gameId AND gg.isWinner = true",
                String.class)
                .setParameter("gameId", gameId)
                .getSingleResult();
    }

    @Override
    public void setWinnerGame(String username, long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            GameGamer gameGamer = em.createQuery(
                    "SELECT gg FROM GameGamer gg WHERE gg.game.id = :gameId AND gg.gamer.username = :username",
                    GameGamer.class)
                    .setParameter("gameId", gameId)
                    .setParameter("username", username)
                    .getSingleResult();
            gameGamer.setWinner(true);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void setGameIsFinished(long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            game.setGameIsFinished();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<MoveResult> findAllMovesGameGamer(String username, long gameId) {
        return em.createQuery(
                "SELECT m FROM MoveResult m WHERE m.gamer.username = :username AND m.game.id = :gameId",
                MoveResult.class)
                .setParameter("username", username)
                .setParameter("gameId", gameId)
                .getResultList();
    }

    @Override
    public Game findGameById(long gameId) {
        return em.find(Game.class, gameId);
    }

}
