package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.PlayerImpl;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PokerPlayer extends PlayerImpl implements Comparable{
    private final List<Card> cards;
    private BetOption betOptionSelected;
    private int bet;
    private final Table table;
    private HandScore handScore;

    public PokerPlayer(ClientHandler clientHandler, Table table) {
        super(clientHandler, 100);
        cards = new ArrayList<>();
        betOptionSelected = null;
        this.table = table;
    }

    public void allIn() {
        betOptionSelected = BetOption.ALL_IN;
        this.bet += currentBalance;
        table.addBet(currentBalance);
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
        table.addBet(amount);
    }

    public void call(int amount) {
        betOptionSelected = BetOption.CALL;
        if(currentBalance - amount < 0) {
            amount = currentBalance;
            betOptionSelected = BetOption.ALL_IN;
        }
        this.bet += amount;
        currentBalance -= amount;
        table.addBet(amount);
    }

    public void fold() {
        betOptionSelected = BetOption.FOLD;
        resetBet();
        table.receiveCardsFromPlayer(returnCards());
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
        table.addBet(amount);
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
        Player player = (Player) o;
        return this.handScore.compareTo(player.handScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(clientHandler, player.clientHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientHandler);
    }
}
