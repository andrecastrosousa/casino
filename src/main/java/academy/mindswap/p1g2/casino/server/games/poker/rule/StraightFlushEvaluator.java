package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StraightFlushEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        // Sort the cards in ascending order based on their value
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)));

        // Check if the hand contains a flush
        AtomicReference<Card.Suit> suitFlush = new AtomicReference<>(Card.Suit.CLUBS);
        boolean isFlush = Arrays.stream(Card.getSuits())
                .anyMatch(suit -> {
                    if(cards.stream()
                            .filter(card -> card.getSuit() == suit)
                            .count() >= 5) {
                        suitFlush.set(suit);
                        return true;
                    }
                    return false;
                });

        if(isFlush) {
            boolean isStraight = true;
            int countVerifiedCards = 0;
            int indexOfLastCombinationCard = 0;

            List<Card> cardsFlush = cards.stream()
                    .filter(card -> card.getSuit() == suitFlush.get())
                    .toList();

            for (int j = 1; j < cardsFlush.size(); j++) {
                if (countVerifiedCards == 4) {
                    isStraight = true;
                    break;
                }
                if (cardsFlush.get(j).getCardValueOnPoker() == cardsFlush.get(j - 1).getCardValueOnPoker() - 1) {
                    countVerifiedCards++;
                    indexOfLastCombinationCard = j;
                } else {
                    isStraight = false;
                    countVerifiedCards = 0;
                }
            }

            if (isStraight || countVerifiedCards == 4) {
                return new HandScore(9000, cardsFlush.subList(indexOfLastCombinationCard - 4, indexOfLastCombinationCard + 1));
            }
        }

        // Pass the evaluation to the next evaluator in the chain
        return evaluateNext(cards);
    }
}
