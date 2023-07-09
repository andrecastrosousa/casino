package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.Comparator;
import java.util.List;

public class StraightFlushEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        // Sort the cards in ascending order based on their value
        cards.sort(Comparator.comparingInt(Card::getCardValueOnPoker));

        // Check if the hand contains a straight flush
        for (int i = 0; i <= cards.size() - 5; i++) {
            boolean isStraightFlush = true;
            Card.Suit suit = cards.get(i).getSuit();

            for (int j = i + 1; j < i + 5; j++) {
                if (cards.get(j).getSuit() != suit ||
                        cards.get(j).getValue().getPokerValue() != cards.get(j - 1).getValue().getPokerValue() + 1) {
                    isStraightFlush = false;
                    break;
                }
            }

            if (isStraightFlush) {
                return 9000;
            }
        }

        // Pass the evaluation to the next evaluator in the chain
        return evaluateNext(cards);
    }
}
