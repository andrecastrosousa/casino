package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;

import java.io.IOException;
import java.util.*;

public class Dealer {
    private final List<Card> cards;
    private final List<Card> tableCards;
    private final List<Card> turnDownCards;

    public Dealer() {
        cards = DeckGenerator.getDeckOfCards();
        tableCards = new ArrayList<>();
        turnDownCards = new ArrayList<>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
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

    public void turnUpCards() {
        if(tableCards.size() < 3) {
            for(int i = 0; i < 3; i++) {
                tableCards.add(cards.remove(cards.size() - 1));
                turnDownCards.add(cards.remove(cards.size() - 1));
            }
        }
    }

    public void nextHand() {
        cards.addAll(tableCards);
        cards.addAll(turnDownCards);
        shuffle();
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        cards.add(0, cards.get(0));
        cards.add(0, cards.get(1));
    }
}
