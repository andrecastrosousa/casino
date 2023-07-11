package academy.mindswap.p1g2.casino.server.games.slotMachine;
import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Slot implements Spot {
    private final List<Player> players;
    private final List<Player> playersPlaying;
    private int currentPlayerPlaying;
    private PlaySound winSound;


    public Slot(List<ClientHandler> clientHandlers) {
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        clientHandlers.forEach(clientHandler -> {
            Player player = new Player(clientHandler, new SlotMachine());
            players.add(player);
            playersPlaying.add(player);
            clientHandler.changeSpot(this);
        });
    }
    private Player getPlayerByClient(ClientHandler clientHandler) {
        return players.stream().filter(player -> player.getClientHandler().equals(clientHandler)).findFirst().orElse(null);
    }
    private void playWinSound() {
        winSound.play();
    }
    public void play() throws IOException {


        while (!gameEnded()) {
            Player currentPlayer = playersPlaying.get(currentPlayerPlaying);

            broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
            currentPlayer.getClientHandler().sendMessageUser(Messages.YOUR_TURN);
            currentPlayer.startTurn();

            while (currentPlayer.isPlaying()) {

            }
            if (currentPlayer.getBalance() == 0) {
                playersPlaying.remove(currentPlayer);
                currentPlayerPlaying --;
                players.remove(currentPlayer);
                currentPlayer.getClientHandler().sendMessageUser(Messages.SLOT_MACHINE_GAMEOVER);
            }
            if (playersPlaying.size() == 1) {
                broadcast(String.format(Messages.WINNER, playersPlaying.get(0).getClientHandler().getUsername()), playersPlaying.get(0).getClientHandler());
                playersPlaying.get(0).getClientHandler().sendMessageUser(Messages.YOU_WON);
                playWinSound();
            } else {
                currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
            }
        }
    }
    private boolean gameEnded() {
        return players.size() <= 1;
    }
    public void doubleBet(ClientHandler clientHandler) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        try {
            player.doubleBet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();
    }

    public void spin(ClientHandler clientHandler) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        try {
            player.spin();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.releaseTurn();

    }
    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster){
        players
                .stream().filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
                .forEach(player -> {
                    try {
                        player.getClientHandler().sendMessageUser(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    @Override
    public void whisper(String message, String clientToSend) {
        players.stream()
                .filter(player -> Objects.equals(player.getClientHandler().getUsername(), clientToSend))
                .forEach(player -> {
                    try {
                        player.getClientHandler().sendMessageUser(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
        clientHandler.sendMessageUser(SpinOption.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if(playerToRemove != null) {
            players.remove(playerToRemove);
            playerToRemove.getClientHandler().closeConnection();
        }
    }

}
