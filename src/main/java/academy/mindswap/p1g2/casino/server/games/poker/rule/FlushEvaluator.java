package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FlushEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        AtomicReference<Card.Suit> suitFlush = new AtomicReference<>(Card.Suit.CLUBS);
        boolean isFlush = Arrays.stream(Card.Suit.values())
                .anyMatch(suit -> {
                    if(cards.stream()
                            .filter(card -> card.getSuit() == suit)
                            .count() >= 5) {
                        suitFlush.set(suit);
                        return true;
                    }
                    return false;
                });

        if (isFlush) {
            List<Card> newHand = cards.stream()
                    .filter(card -> card.getSuit() == suitFlush.get())
                    .sorted(Comparator.comparingInt(Card::getCardValueOnPoker))
                    .toList();

            return new HandScore(6000, newHand.size() == 5 ? newHand : newHand.subList(newHand.size() - 5, newHand.size()));
        }
        return evaluateNext(cards);
    }
}
