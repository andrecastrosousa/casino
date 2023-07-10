package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Poker implements Spot {
    private final Table table;

    public Poker(List<ClientHandler> clientHandlers) {
        table = new Table();
        table.sitDealer(new Dealer());

        clientHandlers.forEach(clientHandler -> {
            table.sitPlayer(new Player(clientHandler, table));
            clientHandler.changeSpot(this);
        });

        createEvaluatorChain();
    }

    private void createEvaluatorChain() {

    }

    private Player getPlayerByClient(ClientHandler clientHandler) {
        return table.getPlayers().stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
    }

    public void play() throws IOException {
        while (!gameEnded()) {
            table.initStreet();
            while (table.isHandOnGoing()) {
                Player currentPlayer = table.getCurrentPlayerPlaying();
                broadcast(String.format("%s is playing. Wait for your turn.", currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.sendMessageToPlayer("It is your time to play.");
                table.playStreet();
            }
            StreetImpl.buildStreet(table).nextStreet();
        }
    }

    private boolean gameEnded() {
        return table.getQuantityOfPlayers() <= 1;
    }

    public void allIn(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.allIn();
        player.releaseTurn();
        System.out.println(player);
    }

    public void call(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.call(20);
        player.releaseTurn();
        System.out.println(player);
    }

    public void check(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.check();
        player.releaseTurn();
        System.out.println(player);
    }

    public void fold(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.fold();
        table.removePlayer(player, false);
        player.releaseTurn();
        System.out.println(player);
    }

    public void raise(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.raise(20);
        player.releaseTurn();
        System.out.println(player);
    }

    public void bet(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.bet(20);
        player.releaseTurn();
        System.out.println(player);
    }

    public void showHand(ClientHandler clientHandler) {
        whisper(table.getPlayerHand(clientHandler), clientHandler.getUsername());
    }

    public void showTableCards(ClientHandler clientHandler) {
        whisper(table.showTableCards(), clientHandler.getUsername());
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster){
        table.getPlayers().stream()
                .filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
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
        table.getPlayers().stream()
                .filter(player -> Objects.equals(player.getClientHandler().getUsername(), clientToSend))
                .forEach(player -> {
                    try {
                        player.sendMessageToPlayer(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
        clientHandler.sendMessageUser(BetOption.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if(playerToRemove != null) {
            table.removePlayer(playerToRemove, true);
            playerToRemove.getClientHandler().closeConnection();
        }
    }
}
