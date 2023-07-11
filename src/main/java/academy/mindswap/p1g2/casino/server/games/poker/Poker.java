package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;
import java.util.Comparator;
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
                if(currentPlayer.getCurrentBalance() > 0) {
                    broadcast(String.format("%s is playing. Wait for your turn.", currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                    currentPlayer.sendMessageToPlayer("It is your time to play.");
                }
                table.playStreet();
            }
            StreetImpl.buildStreet(table).nextStreet();
        }
        table.getPlayers().get(0).getClientHandler().sendMessageUser("You won the poker game.");
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
        broadcast(String.format("%s did an all in", clientHandler.getUsername()),clientHandler);
        whisper("You did an all in", clientHandler.getUsername());
        player.releaseTurn();
    }

    public void call(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        int maxBet = table.getHigherBet();

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        } else if(maxBet == 0) {
            clientHandler.sendMessageUser("You can't call because no one bet. You only can /bet, /check or /fold");
            return;
        } else if(player.getBet() == maxBet) {
            clientHandler.sendMessageUser("You can't call because you have the higher bet. You only can /check or /fold");
            return;
        }

        player.call(maxBet - player.getBet());
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format("%s did an all in with %d poker chips", clientHandler.getUsername(), player.getBet()),clientHandler);
            whisper(String.format("You did an all in with %d poker chips, your current balance is %d", player.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format("%s did a call with %d poker chips", clientHandler.getUsername(), maxBet - player.getBet()),clientHandler);
            whisper(String.format("You did a call with %d poker chips, your current balance is %d", maxBet - player.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        }

        player.releaseTurn();
    }

    public void check(ClientHandler clientHandler) throws IOException {
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        } else if(player.getBet() < maxBet ) {
            clientHandler.sendMessageUser("You can't check because someone bet higher. You only can /call, /fold, /allIn or /raise");
            return;
        }
        player.check();
        broadcast(String.format("%s did a check.", clientHandler.getUsername()),clientHandler);
        whisper("You did a check", clientHandler.getUsername());
        player.releaseTurn();
    }

    public void fold(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.fold();
        broadcast(String.format("%s did a fold.", clientHandler.getUsername()),clientHandler);
        whisper("You did a fold", clientHandler.getUsername());
        table.removePlayer(player, false);
        player.releaseTurn();
    }

    public void raise(ClientHandler clientHandler) throws IOException {
        int maxBet = table.getHigherBet();

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        } else if(maxBet == 0) {
            clientHandler.sendMessageUser("You can't raise because no one bet. You only can /bet, /fold or /allIn");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.raise(maxBet * 2);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format("%s did an all in with %d poker chips", clientHandler.getUsername(), player.getBet()),clientHandler);
            whisper(String.format("You did an all in with %d poker chips, your current balance is %d", player.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format("%s did a raise with %d poker chips", clientHandler.getUsername(), player.getBet()),clientHandler);
            whisper(String.format("You did a raise with %d poker chips, your current balance is %d", player.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        }

        player.releaseTurn();
    }

    public void bet(ClientHandler clientHandler) throws IOException {
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        } else if(maxBet > 0) {
            clientHandler.sendMessageUser("You can't bet because someone already did it. You only can /call, /raise, /allIn or /fold");
            return;
        }
        player.bet(20);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format("%s did an all in with %d poker chips", clientHandler.getUsername(), player.getBet()),clientHandler);
            whisper(String.format("You did an all in with %d poker chips, your current balance is %d", player.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format("%s did a bet with 20 poker chips", clientHandler.getUsername()),clientHandler);
            whisper(String.format("You did a bet with 20 poker chips, your current balance is %d", player.getCurrentBalance()), clientHandler.getUsername());
        }

        player.releaseTurn();
    }

    public void showHand(ClientHandler clientHandler) {
        whisper(table.getPlayerHand(clientHandler), clientHandler.getUsername());
    }

    public void showTableCards(ClientHandler clientHandler) {
        whisper(table.showTableCards(), clientHandler.getUsername());
    }

    public void showBalance(ClientHandler clientHandler) {
        whisper(String.format("Current balance %d", getPlayerByClient(clientHandler).getCurrentBalance()), clientHandler.getUsername());
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
