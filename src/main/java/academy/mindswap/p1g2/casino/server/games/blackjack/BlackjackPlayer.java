package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.deck.BoardChecker;
import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.PlayerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackjackPlayer extends PlayerImpl {
    private final ArrayList<Card> hand;
    private int score;

    public BlackjackPlayer(ClientHandler clientHandler) {
        super(clientHandler);
        hand = new ArrayList<>(2);
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

    public String showCards() {
        BoardChecker boardChecker = BoardChecker.getInstance();
        return boardChecker.getBoard(hand);
    }

    public List<Card> returnCards(String message) throws IOException {
        List<Card> cards = new ArrayList<>(hand);
        hand.clear();
        score = 0;
        sendMessage(message);
        return cards;
    }
}

