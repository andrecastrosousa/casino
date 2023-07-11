package academy.mindswap.p1g2.casino.server.games.deck;

import java.util.ArrayList;
import java.util.List;

public class DeckGenerator {

    private DeckGenerator() {
    }

    public static List<Card> getDeckOfCards() {
        List<Card> deck = new ArrayList<>();
        for(Card.Suit suit : Card.getSuits()) {
            for (Card.Value value: Card.getCardValues()) {
                deck.add(new Card(value, suit));
            }
        }

        return deck;
    }
}
