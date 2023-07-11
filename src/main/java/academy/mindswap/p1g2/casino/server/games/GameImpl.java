package academy.mindswap.p1g2.casino.server.games;

import java.util.ArrayList;
import java.util.List;
import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.slotMachine.SlotMachine;
import academy.mindswap.p1g2.casino.server.games.slotMachine.SlotPlayer;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;

import java.io.IOException;
import java.util.Objects;

public abstract class GameImpl implements Game {
    protected final List<Player> players;
    protected final List<Player> playersPlaying;

    protected GameImpl(List<ClientHandler> clientHandlers) {
        this.players = new ArrayList<>();
        this.playersPlaying = new ArrayList<>();

        clientHandlers.forEach(clientHandler -> {
            Player player = new SlotPlayer(clientHandler, new SlotMachine());
            players.add(player);
            playersPlaying.add(player);
            clientHandler.changeSpot(this);
        });
    }

    @Override
    public boolean gameEnded() {
        return players.size() <= 1;
    }

    protected Player getPlayerByClient(ClientHandler clientHandler) {
        return players.stream().filter(player -> player.getClientHandler().equals(clientHandler)).findFirst().orElse(null);
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster) {
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



