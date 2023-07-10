package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player implements Comparable {
    private final ClientHandler clientHandler;
    private int currentBalance;
    private List<Card> cards;
    private volatile boolean isPlaying;
    private BetOption betOptionSelected;
    private Table table;
    private HandScore handScore;

    public Player(ClientHandler clientHandler, Table table) {
        this.clientHandler = clientHandler;
        currentBalance = 100;
        cards = new ArrayList<>();
        isPlaying = false;
        betOptionSelected = null;
        this.table = table;
    }

    public void addBalance(int balance) {
        this.currentBalance += balance;
    }

    public int allIn() {
        currentBalance = 0;
        return currentBalance;
    }

    public void call(int amount) {
        currentBalance -= amount;
    }

    public List<Card> fold() {
        List<Card> cardsToRetrieve = new ArrayList<>(cards);
        cards.clear();
        return cardsToRetrieve;
    }

    public void raise(int amount) {
        currentBalance -= amount;
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        cards.forEach(card -> message.append(card.toString()));
        return message.toString();
    }

    public void startTurn() {
        isPlaying = true;
    }

    public void releaseTurn() {
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void selectBetOption(BetOption option) {
        betOptionSelected = option;
    }

    public void sendMessageToPlayer(String message) throws IOException {
        clientHandler.sendMessageUser(message);
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
