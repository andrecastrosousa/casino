package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.*;

public class RoyalFlushEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        List<Card> royalFlushCards = cards.stream()
                .filter(card -> card.getValue() == Card.Value.ACE ||
                        card.getValue() == Card.Value.KING ||
                        card.getValue() == Card.Value.QUEEN ||
                        card.getValue() == Card.Value.JACK ||
                        card.getValue() == Card.Value.TEN)
                .toList();

        if (royalFlushCards.size() == 5) {
            boolean isRoyalFlush = Arrays.stream(Card.getSuits())
                    .anyMatch(suit -> royalFlushCards.stream()
                            .filter(card -> card.getSuit() == suit)
                            .count() == 5
                    );

            if (isRoyalFlush) {
                return 10000;
            }
        }

        return evaluateNext(cards);
    }
}
