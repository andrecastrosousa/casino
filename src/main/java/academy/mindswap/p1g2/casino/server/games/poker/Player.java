package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final ClientHandler clientHandler;
    private int currentBalance;
    private List<Card> cards;

    public Player(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        currentBalance = 100;
        cards = new ArrayList<>();
    }

    public void addBalance(int balance) {
        this.currentBalance += balance;
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
    }

    public void fold() {
        cards.clear();
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        cards.forEach(card -> message.append(card.toString()));
        return message.toString();
    }
}
