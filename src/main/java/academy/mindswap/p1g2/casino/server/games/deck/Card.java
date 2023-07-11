package academy.mindswap.p1g2.casino.server.games.deck;

import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Objects;

public class Card implements Comparable {
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
        return String.format(suit.getAsciiArt(), value.getSymbol(), value.getSymbol());
    }

    @Override
    public int compareTo(Object o) {
        Card card = (Card) o;
        return getCardValueOnPoker() - card.getCardValueOnPoker();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value.getPokerValue() == card.value.getPokerValue();
    }

    public int getCardValueOnBlackjack(){
        return value.getBlackjackValue();
    }

    public enum Value {
        ACE('A', 14, 1),
        TWO('2', 2,2),
        THREE('3', 3,3),
        FOUR('4', 4,4),
        FIVE('5', 5,5),
        SIX('6', 6,6),
        SEVEN('7', 7,7),
        EIGHT('8',8,8),
        NINE('9', 9,9),
        TEN('T', 10,10),
        JACK('J', 11,11),
        QUEEN('Q', 12,12),
        KING('K', 13,13);

        private final char symbol;
        private final int pokerValue;
        private final int blackjackValue;

        Value(char symbol, int pokerValue, int blackjackValue) {
            this.symbol = symbol;
            this.pokerValue = pokerValue;
            this.blackjackValue = blackjackValue;
        }

        public char getSymbol() {
            return symbol;
        }

        public int getPokerValue() {
            return pokerValue;
        }

        public int getBlackjackValue() {
            return blackjackValue;
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
