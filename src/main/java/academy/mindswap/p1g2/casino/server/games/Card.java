package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.utils.Messages;

public class Card {
    private final char value;

    private final CardSuit suit;

    public Card(char value, CardSuit suit) {
        this.value = value;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return String.format(suit.getAsciiArt(), value);
    }
}
