package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.Player;

import java.util.List;

public interface GameManager {
    List<Player> getPlayers();
    void sitPlayer(Player player);
    Player getCurrentPlayerPlaying();
    void startTurn();
    void removePlayer(Player player);
    int getQuantityOfPlayers();

}
