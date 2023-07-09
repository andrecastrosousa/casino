package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.Arrays;
import java.util.List;

public class FlushEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        boolean isFlush = Arrays.stream(Card.Suit.values())
                .anyMatch(suit -> cards.stream()
                        .filter(card -> card.getSuit() == suit)
                        .count() == 5
                );

        if (isFlush) {
            return 6000;
        }
        return 0;
    }
}
