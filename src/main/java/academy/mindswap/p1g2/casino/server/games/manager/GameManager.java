package academy.mindswap.p1g2.casino.server.games.manager;

import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.List;

public interface GameManager {
    Player getCurrentPlayerPlaying();

    List<Player> getPlayers();

    void sitPlayer(Player player);

    int getQuantityOfPlayers();

    List<Player> getPlayersPlaying();

    void removePlayer(Player player);

    void releaseTurn();

    void startTurn() throws InterruptedException, IOException;

}
