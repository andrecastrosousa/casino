package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;
import academy.mindswap.p1g2.casino.server.games.poker.street.PreFlopStreet;
import academy.mindswap.p1g2.casino.server.games.poker.street.Street;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetImpl;
import academy.mindswap.p1g2.casino.server.games.poker.street.StreetType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table {
    private final List<Card> cards;
    private final List<Card> tableCards;
    private final List<Card> turnDownCards;
    private StreetType streetType;

    public Table() {
        cards = DeckGenerator.getDeckOfCards();
        tableCards = new ArrayList<>();
        turnDownCards = new ArrayList<>();
        streetType = StreetType.PRE_FLOP;
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
        streetType = StreetImpl.buildStreet(streetType).nextStreet();

        if(tableCards.size() < 3) {
            turnDownCards.add(cards.remove(cards.size() - 1));
            for(int i = 0; i < 3; i++) {
                tableCards.add(cards.remove(cards.size() - 1));
            }
        }
    }

    public void nextHand() {
        cards.addAll(tableCards);
        cards.addAll(turnDownCards);
        shuffle();
        streetType = StreetType.PRE_FLOP;
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        cards.add(0, cards.get(0));
        cards.add(0, cards.get(1));
    }
}
