package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.Dealer;
import academy.mindswap.p1g2.casino.server.games.poker.Player;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private Dealer dealer;
    private final List<Player> players;
    private final List<Player> playersPlaying;
    private int currentPlayerPlaying;

    private final TableManager tableManager;

    public Table() {
        currentPlayerPlaying = 0;
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();
        tableManager = new TableManager();
    }

    public Player getCurrentPlayerPlaying() {
        return playersPlaying.get(currentPlayerPlaying);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void sitDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public void sitPlayer(Player player) {
        players.add(player);
        playersPlaying.add(player);
    }

    public int getQuantityOfPlayers() {
        return players.size();
    }

    public boolean isHandOnGoing() {
        return tableManager.isHandOnGoing();
    }

    public StreetType getStreetType() {
        return tableManager.getStreetType();
    }

    public void burnCard() {
        tableManager.burnCard(dealer.giveCard());
    }

    public void turnUpCard() {
        tableManager.turnUpCard(dealer.giveCard());
    }

    public void setStreetType(StreetType streetType) {
        tableManager.setStreetType(streetType);
    }

    public List<Card> getCards() {
        return tableManager.getCards();
    }

    public String getPlayerHand(ClientHandler clientHandler) {
        Player playerFound = players.stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
        if(playerFound != null) {
            return playerFound.showCards();
        }
        return "";
    }

    public void startHand() {
        dealer.shuffle();
        dealer.distributeCards(players);

        tableManager.setHandOnGoing(true);
    }

    public void startStreet() {
        Player currentPlayer = getCurrentPlayerPlaying();

        currentPlayer.startTurn();

        while (currentPlayer.isPlaying()) {

        }

        tableManager.setHandOnGoing(handEnded());
    }

    public void removePlayer(Player player, boolean fromTable) {
        if(fromTable) {
            players.remove(player);
        }
        playersPlaying.remove(player);
        dealer.receiveCardsFromPlayer(player.fold());
    }

    public boolean handEnded() {
        if(playersPlaying.size() == 1) {
            currentPlayerPlaying = 0;
            return true;
        }
        currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
        return false;
    }
}