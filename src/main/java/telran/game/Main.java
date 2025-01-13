package telran.game;

import telran.game.db.BullCowService;
import telran.game.db.jpa.BullCowServiceImpl;
import telran.game.db.jpa.BullsCowsRepositoryJpaImp;
import telran.game.db.BullsCowsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Настройка подключения к базе данных
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GamePersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        // Инициализация репозитория и сервиса
        BullsCowsRepository repository = new BullsCowsRepositoryJpaImp(entityManager);
        BullCowService service = new BullCowServiceImpl(repository);

        // Запуск сервера на порту 8080
        GameServer server = new GameServer(service);
        try {
            server.startServer(8080);  // Запуск сервера на порту 8080
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server failed to start!");
        }

        // Закрытие EntityManager после завершения работы
        entityManager.close();
        entityManagerFactory.close();
    }
}
