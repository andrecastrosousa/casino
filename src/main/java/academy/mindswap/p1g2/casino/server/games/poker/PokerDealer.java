package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.DealerImpl;
import academy.mindswap.p1g2.casino.server.games.deck.DeckGenerator;
import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.*;
public class PokerDealer extends DealerImpl {

    public PokerDealer() {
        super(DeckGenerator.getDeckOfCards());
    }

    public void pickTableCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    @Override
    public void distributeCards(List<Player> players) {

        for(int i = 0; i < 2; i++) {
            for(Player player : players) {
                ((PokerPlayer) player).receiveCard(cards.remove(cards.size() - 1));
            }
        }

        players.forEach(player -> {
            try {
                player.sendMessage(((PokerPlayer) player).showCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
