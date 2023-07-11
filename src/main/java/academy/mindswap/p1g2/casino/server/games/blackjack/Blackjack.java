package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.*;

import java.io.IOException;
import java.util.ArrayList;
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
    private final BlackjackDealer blackjackDealer;
    private int currentPlayerPlaying;
    private int handCount;
    private PlaySound hitSound;
    private PlaySound winSound;

    private List<Player> playersNotBurst;

    public Blackjack() {
            hitSound = new PlaySound("../casino/sounds/hit_sound.wav");
            winSound = new PlaySound("../casino/sounds/you_win_sound.wav");

        this.players = new ArrayList<>();
        this.playersPlaying = new ArrayList<>();
        playersNotBurst = new ArrayList<>();
        blackjackDealer = new BlackjackDealer();
        handCount = 0;
        currentPlayerPlaying = 0;
    }

    @Override
    public void join(List<ClientHandler> clientHandlers) {
        clientHandlers.forEach(clientHandler -> {
            Player player = new BlackjackPlayer(clientHandler);
            players.add(player);
            playersPlaying.add(player);
            clientHandler.changeSpot(this);
        });
    }

    public void play() throws IOException {
        while (gameEnded()) {
            blackjackDealer.shuffle();
            blackjackDealer.distributeCards(players);
            playersNotBurst = players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
            while (currentPlayerPlaying < playersNotBurst.size()) {
                Player currentPlayer = playersNotBurst.get(currentPlayerPlaying);
                broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.sendMessage(Messages.YOUR_TURN);
                currentPlayer.startTurn();

                while (currentPlayer.isPlaying()) {}
                currentPlayerPlaying++;
            }
            currentPlayerPlaying = 0;
            handCount++;

            blackjackDealer.resetHand();
        }
        winSound.play();
    }
    private void playHitSound() {
        hitSound.play();
    }


    public void hit(ClientHandler clientHandler) throws IOException {
            playHitSound();
            Player currentPlayer = players.get(currentPlayerPlaying);

        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        BlackjackPlayer blackjackPlayer = (BlackjackPlayer) currentPlayer;
        blackjackPlayer.receiveCard(blackjackDealer.giveCard());
        if (blackjackPlayer.getScore() > HIGH_SCORE) {
            blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("Your hand burst!!!"));
        } else {
            currentPlayer.sendMessage(blackjackPlayer.showCards());
        }
        currentPlayer.releaseTurn();
    }

    public void stand(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = playersNotBurst.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.releaseTurn();
    }

    public void surrender(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = playersNotBurst.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            currentPlayer.sendMessage(Messages.NOT_YOUR_TURN);
            return;
        }
        blackjackDealer.receiveCardsFromPlayer(((BlackjackPlayer) currentPlayer).returnCards("You give up!!!"));
        currentPlayer.releaseTurn();
    }

    @Override
    public boolean gameEnded() {
        return handCount <= 3;
    }
}


