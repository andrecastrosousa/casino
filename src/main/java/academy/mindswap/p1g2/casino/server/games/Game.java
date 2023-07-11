package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.Spot;

public interface Game extends Spot {
    void play();
    boolean gameEnded();

}
