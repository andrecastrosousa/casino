package academy.mindswap.p1g2.casino.server.games.manager;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;
import java.util.List;

public interface Game extends Spot {
    void play() throws IOException, InterruptedException;

    boolean gameEnded();

    void join(List<ClientHandler> clientHandlers);
}
