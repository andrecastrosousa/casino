package academy.mindswap.p1g2.casino.server.games.deck;

import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.List;

public class BoardThreeCards implements Board{
    @Override
    public boolean canHandle(int cardsSize) {
        return cardsSize == 3;
    }

    @Override
    public String handle(List<Card> cards) {
        return String.format(Messages.CARD_BOARD3, cards.get(0).getValue().getSymbol(), cards.get(1).getValue().getSymbol(), cards.get(2).getValue().getSymbol(), cards.get(0).getSuit().getAsciiArt(), cards.get(1).getSuit().getAsciiArt(), cards.get(2).getSuit().getAsciiArt(), cards.get(0).getValue().getSymbol(), cards.get(1).getValue().getSymbol(), cards.get(2).getValue().getSymbol());
    }
}
