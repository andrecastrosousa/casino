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
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)));

        AtomicReference<Card.Suit> suitFlush = new AtomicReference<>(Card.Suit.CLUBS);

        if(isFlush(cards, suitFlush)) {
            List<Card> cardsFlush = cards.stream()
                    .filter(card -> card.getSuit() == suitFlush.get())
                    .toList();

            int indexOfLastCombinationCard = isStraight(cardsFlush);

            if (indexOfLastCombinationCard > 0) {
                return new HandScore(9000, cardsFlush.subList(indexOfLastCombinationCard - 4, indexOfLastCombinationCard + 1));
            }
        }

        return evaluateNext(cards);
    }
}
