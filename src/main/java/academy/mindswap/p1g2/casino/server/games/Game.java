package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public interface Game extends Spot {
    void play() throws IOException, InterruptedException;
    boolean gameEnded();
}
