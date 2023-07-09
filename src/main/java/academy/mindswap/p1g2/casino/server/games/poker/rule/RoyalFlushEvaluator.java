package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class RoyalFlushEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> royalFlushCards = cards.stream()
                .filter(card -> card.getValue() == Card.Value.ACE ||
                        card.getValue() == Card.Value.KING ||
                        card.getValue() == Card.Value.QUEEN ||
                        card.getValue() == Card.Value.JACK ||
                        card.getValue() == Card.Value.TEN)
                .toList();

        if (royalFlushCards.size() >= 5) {
            AtomicReference<Card.Suit> suitFlush = new AtomicReference<>(Card.Suit.CLUBS);
            boolean isFlush = Arrays.stream(Card.getSuits())
                    .anyMatch(suit -> {
                        if(cards.stream()
                                .filter(card -> card.getSuit() == suit)
                                .count() == 5) {
                            suitFlush.set(suit);
                            return true;
                        }
                        return false;
                    });

            if (isFlush) {
                return new HandScore(
                        10000,
                        royalFlushCards.stream()
                                .filter(card -> card.getSuit() == suitFlush.get())
                                .toList()
                );
            }
        }

        return evaluateNext(cards);
    }
}
