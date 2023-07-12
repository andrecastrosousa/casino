package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.manager.GameImpl;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;
import academy.mindswap.p1g2.casino.server.games.slotMachine.manager.SlotMachine;
import academy.mindswap.p1g2.casino.server.games.slotMachine.manager.SlotManager;
import academy.mindswap.p1g2.casino.server.games.slotMachine.participant.SlotPlayer;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.List;

public class Slot extends GameImpl {

    private final PlaySound winSound;

    public Slot() {
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        gameManager = new SlotManager();
    }

    private void playWinSound() {
        winSound.play();
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            gameManager.sitPlayer(new SlotPlayer(clientHandler, new SlotMachine()));
            clientHandler.changeSpot(this);
        });
    }

    public void play() throws IOException, InterruptedException {
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        broadcast(Messages.TITLE_SLOT_MACHINE, currentPlayer.getClientHandler());
        currentPlayer.sendMessage(Messages.TITLE_SLOT_MACHINE);
        Thread.sleep(4000);
        while (!gameEnded()) {
            currentPlayer = gameManager.getCurrentPlayerPlaying();

            broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
            currentPlayer.sendMessage(Messages.YOUR_TURN);

            gameManager.startTurn();

            if (gameManager.getQuantityOfPlayers() == 1) {
                broadcast(String.format(Messages.WINNER, gameManager.getPlayersPlaying().get(0).getClientHandler().getUsername()), gameManager.getPlayersPlaying().get(0).getClientHandler());
                gameManager.getPlayersPlaying().get(0).sendMessage(Messages.YOU_WON);
                playWinSound();
            }
        }
    }

    public void doubleBet(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer) player).doubleBet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameManager.releaseTurn();
    }

    public void spin(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer) player).spin();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameManager.releaseTurn();
    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        gameManager.getPlayers().forEach(player -> {
            if (!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append(" -> ").append(player.getCurrentBalance()).append(" balance\n").append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(Commands.listCommands());
        player.sendMessage(SpinOption.listCommands());
    }
}
