package academy.mindswap.p1g2.casino.server;

import java.io.IOException;

public interface Spot {
    void removeClient(ClientHandler clientHandler);

    void broadcast(String message, ClientHandler clientHandler);

    void listCommands(ClientHandler clientHandler) throws IOException;

    void whisper(String message, String username);
    void listUsers(ClientHandler clientHandler) throws IOException;
}
