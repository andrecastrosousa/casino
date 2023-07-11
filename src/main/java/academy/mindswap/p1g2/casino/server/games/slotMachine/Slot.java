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
    private int currentPlayerPlaying;
    private final PlaySound winSound;

    public Slot() {
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        this.players = new ArrayList<>();
        this.playersPlaying = new ArrayList<>();
    }

    public Player getPlayerByClient(ClientHandler clientHandler) {
        return players.stream().filter(player -> player.getClientHandler().equals(clientHandler)).findFirst().orElse(null);
    }
    private void playWinSound() {
        winSound.play();
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            Player player = new SlotPlayer(clientHandler, new SlotMachine());
            players.add(player);
            playersPlaying.add(player);
            clientHandler.changeSpot(this);
        });
    }

    public void play() throws IOException {
        while (gameEnded()) {
            Player currentPlayer = playersPlaying.get(currentPlayerPlaying);

            broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
            currentPlayer.sendMessage(Messages.YOUR_TURN);
            currentPlayer.startTurn();

            while (currentPlayer.isPlaying()) {}

            if (currentPlayer.getCurrentBalance() == 0) {
                playersPlaying.remove(currentPlayer);
                currentPlayerPlaying --;
                players.remove(currentPlayer);
                currentPlayer.sendMessage(Messages.SLOT_MACHINE_GAMEOVER);
            }
            if (playersPlaying.size() == 1) {
                broadcast(String.format(Messages.WINNER, playersPlaying.get(0).getClientHandler().getUsername()), playersPlaying.get(0).getClientHandler());
                playersPlaying.get(0).sendMessage(Messages.YOU_WON);
                playWinSound();
            } else {
                currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
            }
        }
    }

    public void doubleBet(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer)player).doubleBet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();
    }

    public void spin(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        try {
            ((SlotPlayer)player).spin();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();

    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        players.forEach(player -> {
            if(!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append(" -> ").append(player.getCurrentBalance()).append(" balance\n").append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }
}
