package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.utils.Messages;

public class Card {
    private final Value value;

    private final Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public static Value[] getCardValues() {
        return Value.values();
    }

    public static Suit[] getSuits() {
        return Suit.values();
    }

    public Value getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getCardValueOnPoker() {
        return value.getPokerValue();
    }

    @Override
    public String toString() {
        return String.format(suit.getAsciiArt(), value, value);
    }

    public enum Value {
        ACE('A', 14),
        TWO('2', 2),
        THREE('3', 3),
        FOUR('4', 4),
        FIVE('5', 5),
        SIX('6', 6),
        SEVEN('7', 7),
        EIGHT('8',8),
        NINE('9', 9),
        TEN('T', 10),
        JACK('J', 11),
        QUEEN('Q', 12),
        KING('K', 13);

        private final char symbol;
        private final int pokerValue;

        Value(char symbol, int pokerValue) {
            this.symbol = symbol;
            this.pokerValue = pokerValue;
        }

        public char getSymbol() {
            return symbol;
        }

        public int getPokerValue() {
            return pokerValue;
        }
    }

    public enum Suit {
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
