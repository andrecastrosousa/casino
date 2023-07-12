package academy.mindswap.p1g2.casino.server.games.blackjack.manager;

import academy.mindswap.p1g2.casino.server.games.manager.GameManagerImpl;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.blackjack.participant.BlackjackDealer;
import academy.mindswap.p1g2.casino.server.games.blackjack.participant.BlackjackPlayer;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.List;

public class BlackjackTable extends GameManagerImpl {
    private final BlackjackDealer blackjackDealer;
    private int handCount;
    private final PlaySound winSound;

    public BlackjackTable() {
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        blackjackDealer = new BlackjackDealer();
        handCount = 0;
    }

    public boolean handStillOnGoing() {
        return currentPlayerPlaying < playersPlaying.size();
    }

    public String getDealerCards() {
        return blackjackDealer.showCards();
    }

    public void playerHit() throws IOException {
        Player currentPlayer = getCurrentPlayerPlaying();

        BlackjackPlayer blackjackPlayer = (BlackjackPlayer) currentPlayer;
        blackjackPlayer.receiveCard(blackjackDealer.giveCard());
        if (blackjackPlayer.getScore() > Blackjack.HIGH_SCORE) {
            blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("Your hand burst!!!"));
        } else {
            currentPlayer.sendMessage(blackjackPlayer.showCards());
        }
    }

    public void playerSurrender() throws IOException {
        Player currentPlayer = getCurrentPlayerPlaying();
        blackjackDealer.receiveCardsFromPlayer(((BlackjackPlayer) currentPlayer).returnCards("You give up!!!"));
    }

    public int getHandCount() {
        return handCount;
    }

    public void startHand() {
        blackjackDealer.shuffle();
        blackjackDealer.distributeCards(players);
        playersPlaying = players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
    }

    public void theWinnerIs() throws IOException {
        players.get(0).sendMessage(blackjackDealer.showCards());
        List<Player> players = this.players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
        players.forEach(player -> {
            BlackjackPlayer blackjackPlayer = (BlackjackPlayer) player;
            try {
                if (blackjackDealer.getScore() > blackjackPlayer.getScore() && blackjackDealer.getScore() <= 21) {
                    blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("Dealer wins this round!!!"));
                } else {
                    blackjackDealer.receiveCardsFromPlayer(blackjackPlayer.returnCards("You win!!!"));
                    winSound.play();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        clear();
    }

    private void clear() {
        currentPlayerPlaying = 0;
        handCount++;
        blackjackDealer.resetHand();
    }

    @Override
    public synchronized void startTurn() throws InterruptedException {
        Player currentPlayer = getCurrentPlayerPlaying();
        currentPlayer.startTurn();

        wait();

        currentPlayerPlaying++;
    }
}
