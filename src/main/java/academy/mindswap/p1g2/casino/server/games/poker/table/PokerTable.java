package academy.mindswap.p1g2.casino.server.games.poker.table;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.manager.GameManagerImpl;
import academy.mindswap.p1g2.casino.server.games.poker.participant.PokerDealer;
import academy.mindswap.p1g2.casino.server.games.poker.participant.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class PokerTable extends GameManagerImpl {
    private final PokerDealer pokerDealer;
    private final PokerTableManager pokerTableManager;
    private int playTimes;

    public PokerTable(PokerDealer pokerDealer) {
        pokerTableManager = new PokerTableManager();
        playTimes = 0;
        this.pokerDealer = pokerDealer;
    }

    public boolean isHandOnGoing() {
        return pokerTableManager.isHandOnGoing();
    }

    public StreetType getStreetType() {
        return pokerTableManager.getStreetType();
    }

    public void setStreetType(StreetType streetType) {
        pokerTableManager.setStreetType(streetType);
    }

    public void burnCard() {
        pokerTableManager.burnCard(pokerDealer.giveCard());
    }

    public void turnUpCard() {
        pokerTableManager.turnUpCard(pokerDealer.giveCard());
    }

    public List<Card> getCards() {
        return pokerTableManager.getCards();
    }

    public void addBet(int bet) {
        pokerTableManager.addBetToPot(bet);
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public String showTableCards() {
        StringBuilder message = new StringBuilder();
        pokerTableManager.getCards().forEach(card -> message.append(card.toString()));
        return message.toString();
    }

    public String getPlayerHand(ClientHandler clientHandler) {
        Player playerFound = players.stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
        if (playerFound != null) {
            return ((PokerPlayer) playerFound).showCards();
        }
        return "";
    }

    public void initStreet() throws InterruptedException {
        StreetImpl.buildStreet(this).execute();
        playTimes = 0;
        currentPlayerPlaying = 0;
        players.forEach(player -> {
            ((PokerPlayer) player).resetBet();
        });
    }

    public void resetHand() {
        pokerTableManager.setHandOnGoing(false);
        players.forEach(player -> {
            ((PokerPlayer) player).resetBet();
        });
        playersPlaying.clear();
        playersPlaying.addAll(players);
        pokerDealer.pickTableCards(pokerTableManager.clear());
        pokerTableManager.resetPot();
    }


    public void startHand() {
        pokerDealer.shuffle();
        pokerDealer.distributeCards(players);
        pokerTableManager.setHandOnGoing(true);
    }

    @Override
    public synchronized void startTurn() throws InterruptedException {
        Player currentPlayer = getCurrentPlayerPlaying();

        if (currentPlayer.getCurrentBalance() > 0) {
            currentPlayer.startTurn();
        }

        // Will wait for user to release thread, this happens when user finished successfully the turn
        wait();

        playTimes++;
        if (handContinue()) {
            StreetImpl.buildStreet(this).nextStreet();
            return;
        }
        pokerTableManager.setHandOnGoing(false);
    }

    public void removePlayer(Player player, boolean fromTable) {
        if (fromTable) {
            players.remove(player);
        }
        playersPlaying.remove(player);
        receiveCardsFromPlayer(((PokerPlayer) player).returnCards());
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        pokerDealer.receiveCardsFromPlayer(cards);
    }

    public int getPotValue() {
        return pokerTableManager.getPotValue();
    }

    public boolean handContinue() {
        if (playersPlaying.size() == 1) {
            currentPlayerPlaying = 0;
            Player winnerPlayer = playersPlaying.get(0);

            players.forEach(player -> {
                try {
                    if (player == winnerPlayer) {
                        player.sendMessage(String.format(Messages.YOU_WON_HAND, pokerTableManager.getPotValue()));
                        player.addBalance(pokerTableManager.getPotValue());
                        receiveCardsFromPlayer(((PokerPlayer) player).returnCards());
                    } else {
                        player.sendMessage(String.format(Messages.SOMEONE_WON_HAND, winnerPlayer.getClientHandler().getUsername(), pokerTableManager.getPotValue()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            pokerTableManager.setStreetType(StreetType.SHOWDOWN);
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
