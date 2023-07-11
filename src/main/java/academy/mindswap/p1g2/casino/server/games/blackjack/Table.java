package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final BlackjackDealer blackjackDealer;
    private int currentPlayerPlaying;
    private int handCount;
    private final List<Player> players;
    private List<Player> playersNotBurst;

    public Table() {
        playersNotBurst = new ArrayList<>();
        blackjackDealer = new BlackjackDealer();
        handCount = 0;
        players = new ArrayList<>();
        currentPlayerPlaying = 0;
    }

    public void sitPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayerPlaying() {
        return playersNotBurst.get(currentPlayerPlaying);
    }

    public boolean handStillOnGoing() {
        return currentPlayerPlaying < playersNotBurst.size();
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
        playersNotBurst = players.stream().filter(player -> ((BlackjackPlayer) player).getScore() > 0).toList();
    }

    public void playHand() {
        Player currentPlayer = getCurrentPlayerPlaying();
        currentPlayer.startTurn();

        while (currentPlayer.isPlaying()) {}
        currentPlayerPlaying++;
    }

    public void clear() {
        currentPlayerPlaying = 0;
        handCount++;
        blackjackDealer.resetHand();
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
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
