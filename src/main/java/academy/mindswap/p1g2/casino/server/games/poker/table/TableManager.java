package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private final List<Card> cards;
    private final List<Card> burnedCards;
    private StreetType streetType;
    private final Pot pot;
    private boolean handOnGoing;

    public TableManager() {
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

    public void burnCard(Card card) {
        burnedCards.add(card);
    }

    public void turnUpCard(Card card) {
        cards.add(card);
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> getBurnedCards() {
        return burnedCards;
    }

    public void addBetToPot(int bet) {
        pot.addAmount(bet);
    }

    public void resetPot() {
        pot.resetPot();
    }

    @Override
    public String toString() {
        return "TableManager{" +
                "streetType=" + streetType +
                ", pot=" + pot +
                '}';
    }
}
