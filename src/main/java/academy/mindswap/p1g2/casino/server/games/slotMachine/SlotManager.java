package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlotManager {
    private int currentPlayerPlaying;
    private final List<Player> players;
    private final List<Player> playersPlaying;

    public SlotManager() {
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();
    }

    public Player getCurrentPlayerPlaying() {
        return playersPlaying.get(currentPlayerPlaying);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void sitPlayer(Player player) {
        players.add(player);
        playersPlaying.add(player);
    }

    public synchronized void startTurn() throws IOException, InterruptedException {
        Player currentPlayer = getCurrentPlayerPlaying();
        currentPlayer.startTurn();

        // Will wait for user to release thread, this happens when user finished successfully the turn
        wait();

        if (currentPlayer.getCurrentBalance() == 0) {
            playersPlaying.remove(currentPlayer);
            currentPlayerPlaying --;
            players.remove(currentPlayer);
            currentPlayer.sendMessage(Messages.SLOT_MACHINE_GAMEOVER);
        }

        currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
    }

    public synchronized void releaseTurn() {
        notifyAll();
    }

}
