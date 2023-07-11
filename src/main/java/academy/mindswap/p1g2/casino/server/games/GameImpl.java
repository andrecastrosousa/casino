package academy.mindswap.p1g2.casino.server.games;

import java.util.List;
import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;

import java.io.IOException;
import java.util.Objects;

public abstract class GameImpl implements Game {
    private final List<Player> players;

    protected GameImpl(List<Player> players) {
        this.players = players;
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
    public abstract void removeClient(ClientHandler clientHandler);

}



