package academy.mindswap.p1g2.casino.server.games.poker;

import java.util.List;

public class TableManager {
    private Dealer dealer;
    private List<Player> players;
    private List<Player> playersPlaying;
    private int currentPlayerPlaying;

    public TableManager() {
        currentPlayerPlaying = 0;
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

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public void setPlayer(Player player) {
        players.add(player);
        playersPlaying.add(player);
    }

    public int getQuantityOfPlayers() {
        return players.size();
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
