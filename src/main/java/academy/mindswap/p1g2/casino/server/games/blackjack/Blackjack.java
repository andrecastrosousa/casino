package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.blackjack.commands.BlackjackMove;
import academy.mindswap.p1g2.casino.server.games.blackjack.participant.BlackjackPlayer;
import academy.mindswap.p1g2.casino.server.games.blackjack.manager.BlackjackTable;
import academy.mindswap.p1g2.casino.server.games.manager.GameImpl;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.command.Commands;

import java.io.IOException;
import java.util.List;

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
    private final PlaySound hitSound;

    public Blackjack() {
        gameManager = new BlackjackTable();
        hitSound = new PlaySound("../casino/sounds/hit_sound.wav");
    }

    @Override
    public void play() throws IOException, InterruptedException {
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        broadcast(Messages.TITLE_BLACKJACK, currentPlayer.getClientHandler());
        currentPlayer.sendMessage(Messages.TITLE_BLACKJACK);
        Thread.sleep(4000);
        while (gameEnded()) {
            ((BlackjackTable)gameManager).startHand();
            while (((BlackjackTable) gameManager).handStillOnGoing()) {
                currentPlayer = gameManager.getCurrentPlayerPlaying();
                broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.sendMessage(Messages.YOUR_TURN);
                gameManager.startTurn();
            }
            broadcast(((BlackjackTable) gameManager).getDealerCards(), gameManager.getPlayers().get(0).getClientHandler());
            ((BlackjackTable) gameManager).theWinnerIs();
        }

    }
    private void playHitSound() {
        hitSound.play();
    }


    public void hit(ClientHandler clientHandler) throws IOException {
        playHitSound();
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        ((BlackjackTable) gameManager).playerHit();
        currentPlayer.releaseTurn();
        gameManager.releaseTurn();
    }

    public void stand(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.releaseTurn();
        gameManager.releaseTurn();
    }

    public void surrender(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = gameManager.getCurrentPlayerPlaying();
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        ((BlackjackTable) gameManager).playerSurrender();
        currentPlayer.releaseTurn();
        gameManager.releaseTurn();
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            gameManager.sitPlayer(new BlackjackPlayer(clientHandler));
            clientHandler.changeSpot(this);
        });
    }

    @Override
    public boolean gameEnded() {
        return ((BlackjackTable) gameManager).getHandCount() <= 3;
    }

    @Override
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        gameManager.getPlayers().forEach(player -> {
            if(!player.getClientHandler().equals(clientHandler)) {
                message.append(player.getClientHandler().getUsername()).append("\n");
            }
        });
        message.append("-----------------------------------");
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(message.toString());
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        Player player = getPlayerByClient(clientHandler);
        player.sendMessage(Commands.listCommands());
        player.sendMessage(BlackjackMove.listCommands());
    }
}


