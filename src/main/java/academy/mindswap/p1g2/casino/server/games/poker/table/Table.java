package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.Player;
import academy.mindswap.p1g2.casino.server.games.poker.Dealer;
import academy.mindswap.p1g2.casino.server.games.poker.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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

    public void burnCard() throws InterruptedException {
        tableManager.burnCard(dealer.giveCard());
    }

    public void turnUpCard() throws InterruptedException {
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
            return ((PokerPlayer) playerFound).showCards();
        }
        return "";
    }

    public void initStreet() throws InterruptedException {
        StreetImpl.buildStreet(this).execute();
        playTimes = 0;
        currentPlayerPlaying = 0;
        players.forEach( player -> {
            ((PokerPlayer) player).resetBet();
        });
    }

    public void resetHand() {
        tableManager.setHandOnGoing(false);
        players.forEach( player -> {
            ((PokerPlayer) player).resetBet();
        });
        playersPlaying.clear();
        playersPlaying.addAll(players);
        dealer.pickTableCards(tableManager.clear());
        tableManager.resetPot();
    }

    public void startHand() throws InterruptedException {
        dealer.shuffle();
        dealer.distributeCards(players);

        tableManager.setHandOnGoing(true);
    }

    public void playStreet() throws InterruptedException {
        Player currentPlayer = getCurrentPlayerPlaying();

        if(currentPlayer.getCurrentBalance() > 0) {
            currentPlayer.startTurn();
        }

        while (currentPlayer.isPlaying()) {

        }

        playTimes++;
        if(handContinue()) {
            StreetImpl.buildStreet(this).nextStreet();
            return;
        }
        tableManager.setHandOnGoing(false);
    }

    public void removePlayer(Player player, boolean fromTable) {
        if(fromTable) {
            players.remove(player);
        }
        playersPlaying.remove(player);
        receiveCardsFromPlayer(((PokerPlayer) player).returnCards());
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
                        player.sendMessage(String.format(Messages.YOU_WON_HAND, tableManager.getPotValue()));
                        player.addBalance(tableManager.getPotValue());
                        receiveCardsFromPlayer(((PokerPlayer) player).returnCards());
                    } else {
                        player.sendMessage(String.format(Messages.SOMEONE_WON_HAND, winnerPlayer.getClientHandler().getUsername(), tableManager.getPotValue()));
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

    public int getHigherBet() {
        return getPlayersPlaying().stream()
                .map(player -> ((PokerPlayer) player).getBet())
                .max(Comparator.comparingInt(Integer::intValue))
                .orElse(0);
    }
}
