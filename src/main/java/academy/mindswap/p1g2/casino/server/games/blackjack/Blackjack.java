package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.games.*;

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
    private final BlackjackDealer blackjackDealer;
    private int currentPlayerPlaying;
    private int handCount;
    private PlaySound hitSound;
    private PlaySound winSound;

    public Blackjack(List<ClientHandler> clientHandlerList) {
        super(clientHandlerList);
        hitSound = new PlaySound("../casino/sounds/hit_sound.wav");
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        blackjackDealer = new BlackjackDealer();
        handCount = 0;
        currentPlayerPlaying = 0;
    }

    public void play() throws IOException {
        while (!gameEnded()) {
            blackjackDealer.shuffle();
            blackjackDealer.distributeCards(players);
            List<Player> playersNotBurst = players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
            while (currentPlayerPlaying < playersNotBurst.size()) {
                Player currentPlayer = playersNotBurst.get(currentPlayerPlaying);
                broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.getClientHandler().sendMessageUser(Messages.YOUR_TURN);
                currentPlayer.startTurn();

                while (currentPlayer.isPlaying()) {
                }
                currentPlayerPlaying++;
            }
            currentPlayerPlaying = 0;
            handCount++;

            theWinnerIs();
            blackjackDealer.resetHand();
        }
    }
    private void playHitSound() {
        hitSound.play();
    }
    public void hit(ClientHandler clientHandler) throws IOException, InterruptedException {
        playHitSound();
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        BlackjackPlayer blackjackPlayer = (BlackjackPlayer) currentPlayer;
        blackjackPlayer.receiveCard(blackjackDealer.giveCard());
        if (blackjackPlayer.getScore() > HIGH_SCORE) {
            blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("Your hand burst!!!"));
        } else {
            clientHandler.sendMessageUser(blackjackPlayer.showCards());
        }
        currentPlayer.releaseTurn();
    }

    public void stand(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.releaseTurn();
    }

    public void surrender(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        blackjackDealer.receiveCardsFromPlayer(((BlackjackPlayer) currentPlayer).returnCards("You give up!!!"));
        currentPlayer.releaseTurn();
    }

    private void theWinnerIs() throws IOException {
        broadcast(blackjackDealer.showCards(), players.get(0).getClientHandler());
        players.get(0).sendMessage(blackjackDealer.showCards());
        List<Player> players = this.players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
        players.forEach(player -> {
            BlackjackPlayer blackjackPlayer = (BlackjackPlayer) player;
            try {
                if (blackjackDealer.getScore() > blackjackPlayer.getScore() && blackjackDealer.getScore() <= 21) {
                    blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("Dealer wins this round!!!"));
                } else {
                    blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("You win!!!"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean gameEnded() {
        return handCount > 3;
    }
}


