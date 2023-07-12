package academy.mindswap.p1g2.casino.server.games.manager;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.Objects;

public abstract class GameImpl implements Game {
    protected GameManager gameManager;

    @Override
    public boolean gameEnded() {
        return gameManager.getQuantityOfPlayers() <= 1;
    }

    protected Player getPlayerByClient(ClientHandler clientHandler) {
        return gameManager.getPlayers().stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster) {
        gameManager.getPlayers().stream()
                .filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
                .forEach(player -> {
                    try {
                        player.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void whisper(String message, String clientToSend) {
        gameManager.getPlayers().stream()
                .filter(player -> Objects.equals(player.getClientHandler().getUsername(), clientToSend))
                .forEach(player -> {
                    try {
                        player.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public abstract void listCommands(ClientHandler clientHandler) throws IOException;

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if (playerToRemove != null) {
            gameManager.getPlayers().remove(playerToRemove);
            playerToRemove.getClientHandler().closeConnection();
        }
    }

}



