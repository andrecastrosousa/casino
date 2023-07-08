package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Card> cards;
    private final List<Card> burnedCards;
    private StreetType streetType;
    private Pot pot;
    private TableManager tableManager;
    private boolean handOnGoing;

    public Table() {
        cards = new ArrayList<>();
        burnedCards = new ArrayList<>();
        streetType = StreetType.PRE_FLOP;
        pot = new Pot();
        tableManager = new TableManager();
    }

    public void startHand() {
        Dealer dealer = tableManager.getDealer();
        dealer.shuffle();
        dealer.distributeCards(tableManager.getPlayers());
        handOnGoing = true;
    }

    public void startStreet() {
        Player currentPlayer = tableManager.getCurrentPlayerPlaying();

        currentPlayer.startTurn();

        while (currentPlayer.isPlaying()) {

        }

        handOnGoing = tableManager.handEnded();
    }

    public boolean isHandOnGoing() {
        return handOnGoing;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public void burnCard() {
        Dealer dealer = tableManager.getDealer();
        burnedCards.add(dealer.giveCard());
    }

    public void turnUpCard() {
        Dealer dealer = tableManager.getDealer();
        cards.add(dealer.giveCard());
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    public void sitDealer(Dealer dealer) {
        tableManager.setDealer(dealer);
    }

    public void sitPlayer(Player player) {
        tableManager.setPlayer(player);
    }

    public List<Player> getPlayers() {
        return tableManager.getPlayers();
    }

    public int quantityOfPlayers() {
        return tableManager.getQuantityOfPlayers();
    }

    public Player getCurrentPlayerPlaying() {
        return tableManager.getCurrentPlayerPlaying();
    }

    public void removePlayerFromHand(Player player, boolean fromTable) {
        tableManager.removePlayer(player, fromTable);
    }
}
