package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.utils.Messages;

public class Card {
    private final char value;

    private final Suit suit;

    public Card(char value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public static Value[] getCardValues() {
        return Value.values();
    }

    public static Suit[] getSuits() {
        return Suit.values();
    }

    @Override
    public String toString() {
        return String.format(suit.getAsciiArt(), value, value);
    }

    protected enum Value {
        ACE('A'),
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        TEN('T'),
        JACK('J'),
        QUEEN('Q'),
        KING('K');

        private final char symbol;

        Value(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    protected enum Suit {
        CLUBS(Messages.CARD_CLUB),
        SPADES(Messages.CARD_SPADE),
        HEARTS(Messages.CARD_HEART),
        DIAMONDS(Messages.CARD_DIAMOND);

        private final String asciiArt;

        Suit(String asciiArt) {
            this.asciiArt = asciiArt;
        }

        public String getAsciiArt() {
            return asciiArt;
        }
    }
}
