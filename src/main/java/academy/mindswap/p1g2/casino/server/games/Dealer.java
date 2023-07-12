package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.deck.Card;

import java.util.List;

public interface Dealer {
    void distributeCards(List<Player> players);

    void receiveCardsFromPlayer(List<Card> cards);

    Card giveCard();

    void shuffle() throws InterruptedException;
}
