package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.GameImpl;
import academy.mindswap.p1g2.casino.server.games.Player;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Poker extends GameImpl {
    private final Table table;
    private PlaySound checkSound;
    private PlaySound betSound;
    private PlaySound winSound;

    public Poker(List<ClientHandler> clientHandlers) {
        table = new Table();
        table.sitDealer(new PokerDealer());
        checkSound = new PlaySound("../casino/sounds/check_sound.wav");
        betSound = new PlaySound("../casino/sounds/bet_sound.wav");
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");

        clientHandlers.forEach(clientHandler -> {
            table.sitPlayer(new PokerPlayer(clientHandler, table));
            clientHandler.changeSpot(this);
        });
    }
    private void playCheckSound() {
        checkSound.play();
    }
    private void playBetSound() {
        betSound.play();
    }

    @Override
    protected Player getPlayerByClient(ClientHandler clientHandler) {
        return table.getPlayers().stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void play() throws IOException, InterruptedException  {
        while (gameEnded()) {
            table.initStreet();
            while (table.isHandOnGoing()) {
                Player currentPlayer = table.getCurrentPlayerPlaying();
                if(currentPlayer.getCurrentBalance() > 0) {
                    broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                    currentPlayer.sendMessage(Messages.YOUR_TURN);
                }
                table.playStreet();
            }
            StreetImpl.buildStreet(table).nextStreet();
        }
        table.getPlayers().get(0).getClientHandler().sendMessageUser(Messages.YOU_WON);
        playWinSound();
    }
    private void playWinSound() {
        winSound.play();
    }
    @Override
    public boolean gameEnded() {
        return table.getQuantityOfPlayers() > 1;
    }

    public void allIn(ClientHandler clientHandler) throws IOException {
        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN );
            return;
        }
        Player player = getPlayerByClient(clientHandler);

        PokerPlayer pokerPlayer = (PokerPlayer) player;

        pokerPlayer.allIn();
        broadcast(String.format(Messages.SOMEONE_ALL_IN, clientHandler.getUsername()),clientHandler);
        whisper(Messages.YOU_ALL_IN, clientHandler.getUsername());
        player.releaseTurn();
    }

    public void call(ClientHandler clientHandler) throws IOException {
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        } else if(maxBet == 0) {
            clientHandler.sendMessageUser(Messages.NO_BET_CANT_CALL);
            return;
        } else if(pokerPlayer.getBet() == maxBet) {
            clientHandler.sendMessageUser(Messages.HIGHER_BET_CANT_CALL);
            return;
        }

        int chipsBet = maxBet - pokerPlayer.getBet();
        pokerPlayer.call(chipsBet);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.SOMEONE_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()),clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.SOMEONE_CALL_W_CHIPS, clientHandler.getUsername(), chipsBet),clientHandler);
            whisper(String.format(Messages.YOU_CALL_W_CHIPS_BALANCE, chipsBet, player.getCurrentBalance()), clientHandler.getUsername());
        }

        player.releaseTurn();
    }

    public void check(ClientHandler clientHandler) throws IOException {
        playCheckSound();
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        } else if(pokerPlayer.getBet() < maxBet ) {
            clientHandler.sendMessageUser(Messages.HIGHER_BET_CANT_CHECK);
            return;
        }

        pokerPlayer.check();
        broadcast(String.format(Messages.SOMEONE_CHECK, clientHandler.getUsername()),clientHandler);
        whisper(Messages.YOU_CHECK, clientHandler.getUsername());
        player.releaseTurn();
    }

    public void fold(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }

        pokerPlayer.fold();
        broadcast(String.format(Messages.SOMEONE_FOLD, clientHandler.getUsername()),clientHandler);
        whisper(Messages.YOU_FOLD, clientHandler.getUsername());
        table.removePlayer(player, false);
        player.releaseTurn();
    }

    public void raise(ClientHandler clientHandler) throws IOException {
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        } else if(maxBet == 0) {
            clientHandler.sendMessageUser(Messages.NO_ONE_BET_CANT_RISE);
            return;
        }

        pokerPlayer.raise(maxBet * 2);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.YOU_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()),clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.YOU_RISE_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()),clientHandler);
            whisper(String.format(Messages.YOU_RISE_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        }

        player.releaseTurn();
    }

    public void bet(ClientHandler clientHandler) throws IOException {
        playBetSound();
        int maxBet = table.getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if(!table.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        } else if(maxBet > 0) {
            clientHandler.sendMessageUser(Messages.CANT_BET_SOMEONE_BET);
            return;
        }
        pokerPlayer.bet(20);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.SOMEONE_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()),clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.SOMEONE_BET_20, clientHandler.getUsername()),clientHandler);
            whisper(String.format(Messages.YOU_BET_20, player.getCurrentBalance()), clientHandler.getUsername());
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
        whisper(String.format(Messages.CURRENT_BALANCE, getPlayerByClient(clientHandler).getCurrentBalance()), clientHandler.getUsername());
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
                        player.sendMessage(message);
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
