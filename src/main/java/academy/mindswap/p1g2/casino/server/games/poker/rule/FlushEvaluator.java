package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.participant.HandScore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FlushEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        AtomicReference<Card.Suit> suitFlush = new AtomicReference<>(Card.Suit.CLUBS);

        if (isFlush(cards, suitFlush)) {
            List<Card> newHand = cards.stream()
                    .filter(card -> card.getSuit() == suitFlush.get())
                    .sorted(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)))
                    .toList();

            return new HandScore(
                    EvaluatorType.FLUSH.getScore(),
                    newHand.size() == 5 ? newHand : newHand.subList(0, 5),
                    EvaluatorType.FLUSH
            );
        }
        return evaluateNext(cards);
    }
}
