package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.util.ArrayList;
import java.util.List;

public class PokerTableManager {
    private final List<Card> cards;
    private final List<Card> burnedCards;
    private final Pot pot;
    private StreetType streetType;
    private boolean handOnGoing;

    public PokerTableManager() {
        cards = new ArrayList<>();
        burnedCards = new ArrayList<>();
        streetType = StreetType.PRE_FLOP;
        pot = new Pot();
    }

    public boolean isHandOnGoing() {
        return handOnGoing;
    }

    public void setHandOnGoing(boolean handOnGoing) {
        this.handOnGoing = handOnGoing;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    public void burnCard(Card card) {
        burnedCards.add(card);
    }

    public void turnUpCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addBetToPot(int bet) {
        pot.addAmount(bet);
    }

    public void resetPot() {
        pot.resetPot();
    }

    public List<Card> clear() {
        List<Card> cardsToRemove = new ArrayList<>(cards);
        cardsToRemove.addAll(burnedCards);
        cards.clear();
        burnedCards.clear();
        return cardsToRemove;
    }

    public int getPotValue() {
        return pot.getAmount();
    }
}
