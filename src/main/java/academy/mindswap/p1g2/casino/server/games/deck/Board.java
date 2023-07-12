package academy.mindswap.p1g2.casino.server.games.deck;

import java.util.List;

public interface Board {
    boolean canHandle(int cardsSize);

    String handle(List<Card> cards);

}
