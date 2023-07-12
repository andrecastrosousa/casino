package academy.mindswap.p1g2.casino.server.games.manager;

import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameManagerImpl implements GameManager {
    protected final List<Player> players;
    protected List<Player> playersPlaying;
    protected int currentPlayerPlaying;

    protected GameManagerImpl() {
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();
    }

    @Override
    public Player getCurrentPlayerPlaying() {
        return players.get(currentPlayerPlaying);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void sitPlayer(Player player) {
        players.add(player);
        playersPlaying.add(player);
    }

    @Override
    public int getQuantityOfPlayers() {
        return players.size();
    }

    @Override
    public List<Player> getPlayersPlaying() {
        return playersPlaying;
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
        playersPlaying.remove(player);
    }

    @Override
    public synchronized void releaseTurn() {
        notifyAll();
    }

    @Override
    public abstract void startTurn() throws InterruptedException, IOException;
}
