package academy.mindswap.p1g2.casino.server.games.slotMachine.manager;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.manager.GameManagerImpl;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;

public class SlotManager extends GameManagerImpl {
    public SlotManager() {
    }

    @Override
    public synchronized void startTurn() throws IOException, InterruptedException {
        Player currentPlayer = getCurrentPlayerPlaying();
        currentPlayer.startTurn();

        // Will wait for user to release thread, this happens when user finished successfully the turn
        wait();

        if (currentPlayer.getCurrentBalance() == 0) {
            playersPlaying.remove(currentPlayer);
            currentPlayerPlaying--;
            players.remove(currentPlayer);
            currentPlayer.sendMessage(Messages.SLOT_MACHINE_GAMEOVER);
        }

        currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
    }
}
