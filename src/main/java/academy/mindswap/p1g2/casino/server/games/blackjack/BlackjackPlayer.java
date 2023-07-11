package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.PlayerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackjackPlayer extends PlayerImpl {
    private final ArrayList<Card> hand;
    private int score;

    public BlackjackPlayer(ClientHandler clientHandler) {
        super(clientHandler);
        hand = new ArrayList<Card>(2);
        score = 0;
    }

    public void receiveCard(Card card) {
        hand.add(card);
        score += card.getCardValueOnBlackjack();
    }

    public int getScore() {
        return score;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void sendMessageToPlayer(String message) throws IOException {
        clientHandler.sendMessageUser(message);
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        hand.forEach(card -> message.append(card.toString()));
        return message.toString();
    }

    public List<Card> returnCards(String message) throws IOException {
        List<Card> cards = new ArrayList<>(hand);
        hand.clear();
        score = 0;
        clientHandler.sendMessageUser(message);
        return cards;
    }
    }

