package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private final List<Card> cards;
    private final List<Card> burnedCards;
    private StreetType streetType;
    private Pot pot;
    private boolean handOnGoing;

    public TableManager() {
        cards = new ArrayList<>();
        burnedCards = new ArrayList<>();
        streetType = StreetType.PRE_FLOP;
        pot = new Pot();
    }

    public void startStreet() {
        /*Player currentPlayer = table.getCurrentPlayerPlaying();

        currentPlayer.startTurn();

        while (currentPlayer.isPlaying()) {

        }

        handOnGoing = table.handEnded();*/
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
}