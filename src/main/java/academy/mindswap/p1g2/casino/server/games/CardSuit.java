package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.utils.Messages;

public enum CardSuit {
    CLUBS(Messages.CARD_CLUB),
    SPADES(Messages.CARD_SPADE),
    HEARTS(Messages.CARD_HEART),
    DIAMONDS(Messages.CARD_DIAMOND);

    private final String asciiArt;

    CardSuit(String asciiArt) {
        this.asciiArt = asciiArt;
    }

    public String getAsciiArt() {
        return asciiArt;
    }
}
