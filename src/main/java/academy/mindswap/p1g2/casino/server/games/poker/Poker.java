package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.manager.GameImpl;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.participant.PokerDealer;
import academy.mindswap.p1g2.casino.server.games.poker.participant.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.List;

public class Poker extends GameImpl {
    private final PlaySound checkSound;
    private final PlaySound betSound;
    private final PlaySound winSound;

    public Poker() {
        gameManager = new PokerTable(new PokerDealer());
        checkSound = new PlaySound("../casino/sounds/check_sound.wav");
        betSound = new PlaySound("../casino/sounds/bet_sound.wav");
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
    }

    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            gameManager.sitPlayer(new PokerPlayer(clientHandler, ((PokerTable) gameManager)));
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
    public void play() throws IOException, InterruptedException {
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        broadcast(Messages.TITLE_POKER, currentPlayer.getClientHandler());
        currentPlayer.sendMessage(Messages.TITLE_POKER);
        Thread.sleep(4000);
        while (!gameEnded()) {
            ((PokerTable) gameManager).initStreet();
            while (((PokerTable) gameManager).isHandOnGoing()) {
                currentPlayer = gameManager.getCurrentPlayerPlaying();
                if (currentPlayer.getCurrentBalance() > 0) {
                    broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                    currentPlayer.sendMessage(Messages.YOUR_TURN);
                }
                gameManager.startTurn();
            }
            StreetImpl.buildStreet(((PokerTable) gameManager)).nextStreet();
        }
        gameManager.getPlayers().get(0).sendMessage(Messages.YOU_WON);
        playWinSound();
    }

    private void playWinSound() {
        winSound.play();
    }

    public void allIn(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        playBetSound();
        PokerPlayer pokerPlayer = (PokerPlayer) player;
        pokerPlayer.allIn();
        broadcast(String.format(Messages.SOMEONE_ALL_IN, clientHandler.getUsername()), clientHandler);
        whisper(Messages.YOU_ALL_IN, clientHandler.getUsername());
        gameManager.releaseTurn();
    }

    public void call(ClientHandler clientHandler) throws IOException {
        int maxBet = ((PokerTable) gameManager).getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        } else if (maxBet == 0) {
            player.sendMessage(Messages.NO_BET_CANT_CALL);
            return;
        } else if (pokerPlayer.getBet() == maxBet) {
            player.sendMessage(Messages.HIGHER_BET_CANT_CALL);
            return;
        }

        int chipsBet = maxBet - pokerPlayer.getBet();
        pokerPlayer.call(chipsBet);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.SOMEONE_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()), clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.SOMEONE_CALL_W_CHIPS, clientHandler.getUsername(), chipsBet), clientHandler);
            whisper(String.format(Messages.YOU_CALL_W_CHIPS_BALANCE, chipsBet, player.getCurrentBalance()), clientHandler.getUsername());
        }
        playBetSound();
        gameManager.releaseTurn();
    }

    public void check(ClientHandler clientHandler) throws IOException {
        int maxBet = ((PokerTable) gameManager).getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        } else if (pokerPlayer.getBet() < maxBet) {
            player.sendMessage(Messages.HIGHER_BET_CANT_CHECK);
            return;
        }
        playCheckSound();
        pokerPlayer.check();
        broadcast(String.format(Messages.SOMEONE_CHECK, clientHandler.getUsername()), clientHandler);
        whisper(Messages.YOU_CHECK, clientHandler.getUsername());
        player.releaseTurn();
        gameManager.releaseTurn();
    }

    public void fold(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }

        pokerPlayer.fold();
        broadcast(String.format(Messages.SOMEONE_FOLD, clientHandler.getUsername()), clientHandler);
        whisper(Messages.YOU_FOLD, clientHandler.getUsername());
        ((PokerTable) gameManager).removePlayer(player, false);
        player.releaseTurn();
        gameManager.releaseTurn();
    }

    public void raise(ClientHandler clientHandler) throws IOException {
        int maxBet = ((PokerTable) gameManager).getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;

        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        } else if (maxBet == 0) {
            player.sendMessage(Messages.NO_ONE_BET_CANT_RISE);
            return;
        }

        pokerPlayer.raise(maxBet * 2);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.YOU_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()), clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.YOU_RISE_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()), clientHandler);
            whisper(String.format(Messages.YOU_RISE_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        }
        playBetSound();
        gameManager.releaseTurn();
    }

    public void bet(ClientHandler clientHandler) throws IOException {

        int maxBet = ((PokerTable) gameManager).getHigherBet();
        Player player = getPlayerByClient(clientHandler);
        PokerPlayer pokerPlayer = (PokerPlayer) player;
        if (!gameManager.getCurrentPlayerPlaying().getClientHandler().equals(clientHandler)) {
            player.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        } else if (maxBet > 0) {
            player.sendMessage(Messages.CANT_BET_SOMEONE_BET);
            return;
        }
        pokerPlayer.bet(20);
        if (player.getCurrentBalance() == 0) {
            broadcast(String.format(Messages.SOMEONE_ALL_IN_W_CHIPS, clientHandler.getUsername(), pokerPlayer.getBet()), clientHandler);
            whisper(String.format(Messages.YOU_ALL_IN_W_CHIPS_BALANCE, pokerPlayer.getBet(), player.getCurrentBalance()), clientHandler.getUsername());
        } else {
            broadcast(String.format(Messages.SOMEONE_BET_20, clientHandler.getUsername()), clientHandler);
            whisper(String.format(Messages.YOU_BET_20, player.getCurrentBalance()), clientHandler.getUsername());
        }
        playBetSound();
        gameManager.releaseTurn();
    }

    public void showHand(ClientHandler clientHandler) {
        whisper(((PokerTable) gameManager).getPlayerHand(clientHandler), clientHandler.getUsername());
    }

    public void showTableCards(ClientHandler clientHandler) {
        whisper(((PokerTable) gameManager).showTableCards(), clientHandler.getUsername());
    }

    public void showBalance(ClientHandler clientHandler) {
        whisper(String.format(Messages.CURRENT_BALANCE, getPlayerByClient(clientHandler).getCurrentBalance()), clientHandler.getUsername());
    }


    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(Commands.listCommands());
        player.sendMessage(BetOption.listCommands());
    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        gameManager.getPlayers().forEach(player -> {
            if (!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append(" -> ").append(player.getCurrentBalance()).append(" poker chips\n").append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if (playerToRemove != null) {
            ((PokerTable) gameManager).removePlayer(playerToRemove, true);
            playerToRemove.getClientHandler().closeConnection();
        }
    }
}
