package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.GameImpl;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Slot extends GameImpl {

    private final PlaySound winSound;

    private final SlotManager slotManager;

    public Slot() {
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        this.players = new ArrayList<>();
        slotManager = new SlotManager();
        this.playersPlaying = new ArrayList<>();
    }

    public Player getPlayerByClient(ClientHandler clientHandler) {
        return slotManager.getPlayers().stream().filter(player -> player.getClientHandler().equals(clientHandler)).findFirst().orElse(null);
    }
    private void playWinSound() {
        winSound.play();
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            slotManager.sitPlayer(new SlotPlayer(clientHandler, new SlotMachine()));
            clientHandler.changeSpot(this);
        });
    }

    public void play() throws IOException, InterruptedException {
        while (gameEnded()) {
            Player currentPlayer = slotManager.getCurrentPlayerPlaying();

            broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
            currentPlayer.sendMessage(Messages.YOUR_TURN);

            slotManager.startTurn();

            if (playersPlaying.size() == 1) {
                broadcast(String.format(Messages.WINNER, playersPlaying.get(0).getClientHandler().getUsername()), playersPlaying.get(0).getClientHandler());
                playersPlaying.get(0).sendMessage(Messages.YOU_WON);
                playWinSound();
            }
        }
    }

    public void doubleBet(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if(!slotManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer)player).doubleBet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();
        slotManager.releaseTurn();
    }

    public void spin(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if(!slotManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer)player).spin();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();
        slotManager.releaseTurn();
    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        slotManager.getPlayers().forEach(player -> {
            if(!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append(" -> ").append(player.getCurrentBalance()).append(" balance\n").append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }
}
