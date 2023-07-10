package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.Dealer;
import academy.mindswap.p1g2.casino.server.games.poker.Player;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private Dealer dealer;
    private final List<Player> players;
    private final List<Player> playersPlaying;
    private int currentPlayerPlaying;
    private final TableManager tableManager;
    private int playTimes;

    public Table() {
        currentPlayerPlaying = 0;
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();
        tableManager = new TableManager();
        playTimes = 0;
    }

    public Player getCurrentPlayerPlaying() {
        return players.get(currentPlayerPlaying);
    }

    public List<Player> getPlayers() {
        return players;
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

    public void addBet(int bet) {
        tableManager.addBetToPot(bet);
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public List<Player> getPlayersPlaying() {
        return playersPlaying;
    }

    public String showTableCards() {
        StringBuilder message = new StringBuilder();
        tableManager.getCards().forEach(card -> message.append(card.toString()));
        return message.toString();
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

    public void initStreet() {
        StreetImpl.buildStreet(this).execute();
        playTimes = 0;
        players.forEach(Player::resetBet);
    }

    public void resetHand() {
        tableManager.setHandOnGoing(false);
        players.forEach(Player::resetBet);
        playersPlaying.clear();
        playersPlaying.addAll(players);
        tableManager.resetPot();
    }

    public void startHand() {
        dealer.shuffle();
        dealer.distributeCards(players);

        tableManager.setHandOnGoing(true);
    }

    public void playStreet() {
        Player currentPlayer = getCurrentPlayerPlaying();

        currentPlayer.startTurn();

        while (currentPlayer.isPlaying()) {

        }
        playTimes++;
        tableManager.setHandOnGoing(handContinue());
        StreetImpl.buildStreet(this).nextStreet();
    }

    public void removePlayer(Player player, boolean fromTable) {
        if(fromTable) {
            players.remove(player);
        }
        playersPlaying.remove(player);
        receiveCardsFromPlayer(player.returnCards());
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        dealer.receiveCardsFromPlayer(cards);
    }

    public int getPotValue() {
        return tableManager.getPotValue();
    }

    public boolean handContinue() {
        if(playersPlaying.size() == 1) {
            currentPlayerPlaying = 0;
            Player winnerPlayer = playersPlaying.get(0);

            players.forEach(player -> {
                try {
                    if (player == winnerPlayer) {
                        player.sendMessageToPlayer(String.format("You won the hand and won %d poker chips", tableManager.getPotValue()));
                        player.addBalance(tableManager.getPotValue());
                        receiveCardsFromPlayer(player.returnCards());
                    } else {
                        player.sendMessageToPlayer(String.format("%s won the hand and won %d poker chips", winnerPlayer.getClientHandler().getUsername(), tableManager.getPotValue()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            tableManager.setStreetType(StreetType.SHOWDOWN);
            return false;
        }
        currentPlayerPlaying = (currentPlayerPlaying + 1) % players.size();
        while (!playersPlaying.contains(players.get(currentPlayerPlaying))) {
            currentPlayerPlaying = (currentPlayerPlaying + 1) % players.size();
        }

        return true;
    }
}
