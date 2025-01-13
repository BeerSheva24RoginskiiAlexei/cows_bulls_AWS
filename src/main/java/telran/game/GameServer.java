package telran.game;

import java.io.*;
import java.net.*;
import java.time.LocalDate;

import telran.game.db.BullCowService;

public class GameServer {
    private final BullCowService service;

    public GameServer(BullCowService service) {
        this.service = service;
    }

    public void startServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket, service).start();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket socket;
    private final BullCowService service;

    public ClientHandler(Socket socket, BullCowService service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received request: " + request);

                // Разбор запроса и выполнение соответствующей логики
                String[] requestParts = request.split(" ");
                String command = requestParts[0];

                switch (command) {
                    case "CREATE_GAME":
                        if (requestParts.length > 1) {
                            String sequence = requestParts[1];
                            // Логика для создания игры
                            service.createGame(sequence);
                            out.println("Game created with sequence: " + sequence);
                        } else {
                            out.println("Error: Game sequence is missing.");
                        }
                        break;

                    case "REGISTER":
                        if (requestParts.length > 2) {
                            String username = requestParts[1];
                            String dateOfBirth = requestParts[2];
                            LocalDate birthDate = LocalDate.parse(dateOfBirth);
                            service.registerUser(username, birthDate);
                            out.println("User registered with username: " + username);
                        } else {
                            out.println("Error: Username or date of birth is missing.");
                        }
                        break;

                    case "JOIN_GAME":
                        if (requestParts.length > 2) {
                            String username = requestParts[1];
                            long gameId = Long.parseLong(requestParts[2]);
                            // Логика для присоединения к игре
                            service.joinToGame(username, gameId);
                            out.println("User " + username + " joined game " + gameId);
                        } else {
                            out.println("Error: Username or game ID is missing.");
                        }
                        break;

                    case "START_GAME":
                        if (requestParts.length > 2) {
                            String username = requestParts[1];
                            long gameId = Long.parseLong(requestParts[2]);
                            // Логика для старта игры
                            service.startGame(username, gameId);
                            out.println("Game " + gameId + " started by " + username);
                        } else {
                            out.println("Error: Username or game ID is missing.");
                        }
                        break;

                    case "MAKE_MOVE":
                        if (requestParts.length > 4) {
                            String username = requestParts[1];
                            long gameId = Long.parseLong(requestParts[2]);
                            String sequence = requestParts[3];
                            int bulls = Integer.parseInt(requestParts[4]);
                            int cows = Integer.parseInt(requestParts[5]);
                            // Логика для выполнения хода
                            service.createMove(username, gameId, sequence, bulls, cows);
                            out.println("Move made in game " + gameId + " by " + username);
                        } else {
                            out.println("Error: Missing parameters for move.");
                        }
                        break;

                    case "GET_GAME_STATUS":
                        if (requestParts.length > 1) {
                            long gameId = Long.parseLong(requestParts[1]);
                            // Логика для получения состояния игры
                            String status = service.getGameStatus(gameId);
                            out.println("Game " + gameId + " status: " + status);
                        } else {
                            out.println("Error: Game ID is missing.");
                        }
                        break;

                    default:
                        out.println("Error: Unknown command.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
