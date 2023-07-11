package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;

import java.io.IOException;
import java.util.*;

public class Dealer {
    private final List<Card> cards;

    public Dealer() {
        cards = DeckGenerator.getDeckOfCards();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card giveCard() {
        return cards.remove(cards.size() - 1);
    }

    public void distributeCards(List<Player> players) {
        for(int i = 0; i < 2; i++) {
            for(Player player : players) {
                player.receiveCard(cards.remove(cards.size() - 1));
            }
        }

        players.forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(player.showCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        for (Card card: cards) {
            this.cards.add(0, card);
        }
    }

    public void pickTableCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
}
