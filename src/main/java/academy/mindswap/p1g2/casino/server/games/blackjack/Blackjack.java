package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.*;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Rules:
 * 52 cards;
 * cards from 2 to 10 has value of 10;
 * jack, queen and king also worth 10;
 * ace values 1 or 11;
 * each player bets against the dealer;
 * each player have two cards faced up to start the round;
 * dealer also has two cards to start the round, one faced up and the other faced down;
 * "hit" - player asks another card (possible specific command for the game?!)
 * "split" - player has two identical cards and ask to split and each card is one bet;
 * "stand" - player don't want more cards;
 * "double down" - increase the initial bet by 100% and take exactly one more card;
 * "surrender" - lose half the bet and end the hand immediately;
 */

public class Blackjack extends GameImpl {
    public static final int HIGH_SCORE = 21;
    private final BlackjackTable blackjackTable;
    private final PlaySound hitSound;

    public Blackjack() {
        blackjackTable = new BlackjackTable();
        hitSound = new PlaySound("../casino/sounds/hit_sound.wav");
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            blackjackTable.sitPlayer(new BlackjackPlayer(clientHandler));
            clientHandler.changeSpot(this);
        });
    }

    public void play() throws IOException, InterruptedException {
        while (gameEnded()) {
            blackjackTable.startHand();
            while (blackjackTable.handStillOnGoing()) {
                Player currentPlayer = blackjackTable.getCurrentPlayerPlaying();
                broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.sendMessage(Messages.YOUR_TURN);
                blackjackTable.startTurn();
            }
            broadcast(blackjackTable.getDealerCards(), blackjackTable.getPlayers().get(0).getClientHandler());
            blackjackTable.theWinnerIs();
            blackjackTable.clear();
        }

    }
    private void playHitSound() {
        hitSound.play();
    }


    public void hit(ClientHandler clientHandler) throws IOException {
        playHitSound();
        Player currentPlayer = blackjackTable.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        blackjackTable.playerHit();
        currentPlayer.releaseTurn();
        blackjackTable.releaseTurn();
    }

    public void stand(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = blackjackTable.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.releaseTurn();
        blackjackTable.releaseTurn();
    }

    public void surrender(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = blackjackTable.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        blackjackTable.playerSurrender();
        currentPlayer.releaseTurn();
        blackjackTable.releaseTurn();
    }

    @Override
    public boolean gameEnded() {
        return blackjackTable.getHandCount() <= 3;
    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        blackjackTable.getPlayers().forEach(player -> {
            if(!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster) {
        blackjackTable.getPlayers().stream().filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
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
        blackjackTable.getPlayers().stream()
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
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(Commands.listCommands());
        player.sendMessage(SpinOption.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if(playerToRemove != null) {
            blackjackTable.removePlayer(playerToRemove);
            playerToRemove.getClientHandler().closeConnection();
        }
    }
}


