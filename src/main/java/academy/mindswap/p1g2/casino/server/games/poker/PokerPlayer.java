package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.PlayerImpl;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PokerPlayer extends PlayerImpl implements Comparable {
    private final List<Card> cards;
    private BetOption betOptionSelected;
    private int bet;
    private final PokerTable pokerTable;
    private HandScore handScore;

    public PokerPlayer(ClientHandler clientHandler, PokerTable pokerTable) {
        super(clientHandler, 100);
        cards = new ArrayList<>();
        betOptionSelected = null;
        this.pokerTable = pokerTable;
    }

    public void allIn() {
        betOptionSelected = BetOption.ALL_IN;
        this.bet += currentBalance;
        pokerTable.addBet(currentBalance);
        currentBalance = 0;
    }


    public void bet(int amount) {
        betOptionSelected = BetOption.BET;
        if(currentBalance - amount  < 0) {
            amount = currentBalance;
            betOptionSelected = BetOption.ALL_IN;
        }
        this.bet += amount;
        currentBalance -= amount;
        pokerTable.addBet(amount);
    }

    public void call(int amount) {
        betOptionSelected = BetOption.CALL;
        if(currentBalance - amount < 0) {
            amount = currentBalance;
            betOptionSelected = BetOption.ALL_IN;
        }
        this.bet += amount;
        currentBalance -= amount;
        pokerTable.addBet(amount);
    }

    public void fold() {
        betOptionSelected = BetOption.FOLD;
        resetBet();
        pokerTable.receiveCardsFromPlayer(returnCards());
    }

    public List<Card> returnCards() {
        List<Card> cardsToRetrieve = new ArrayList<>(cards);
        cards.clear();
        return cardsToRetrieve;
    }

    public void raise(int amount) {
        betOptionSelected = BetOption.RAISE;
        if(currentBalance - amount < 0) {
            amount = currentBalance;
            betOptionSelected = BetOption.ALL_IN;
        }
        currentBalance -= amount;
        this.bet += amount;
        pokerTable.addBet(amount);
    }

    public void check() {
        betOptionSelected = BetOption.CHECK;
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        cards.forEach(card -> message.append(card.toString()));
        return message.toString();
    }

    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setHandScore(HandScore handScore) {
        this.handScore = handScore;
    }

    public HandScore getHandScore() {
        return handScore;
    }

    public void resetBet() {
        bet = 0;
    }

    public int getBet() {
        return bet;
    }

    @Override
    public int compareTo(Object o) {
        PokerPlayer player = (PokerPlayer) o;
        return this.handScore.compareTo(player.handScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(clientHandler, player.getClientHandler());
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientHandler);
    }
}
