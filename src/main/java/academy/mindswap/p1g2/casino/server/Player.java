package academy.mindswap.p1g2.casino.server;

import java.io.IOException;

public interface Player {

    void startTurn();

    void releaseTurn();

    boolean isPlaying();

    void sendMessage(String message) throws IOException;

    void addBalance(int balance);

    int getCurrentBalance();

    ClientHandler getClientHandler();

}
