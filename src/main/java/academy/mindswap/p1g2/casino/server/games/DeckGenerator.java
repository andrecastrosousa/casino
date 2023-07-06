package academy.mindswap.p1g2.casino.server.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DeckGenerator {

    private DeckGenerator() {
    }

    public static List<Card> getDeckOfCards() {
        List<Card> deck = new ArrayList<>();
        for(Card.Suit suit : Card.getSuits()) {
            for (Card.Value value: Card.getCardValues()) {
                deck.add(new Card(value.getSymbol(), suit));
            }
        }

        return deck;
    }
}
