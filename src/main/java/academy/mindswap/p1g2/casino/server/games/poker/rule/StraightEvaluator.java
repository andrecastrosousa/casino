package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.Comparator;
import java.util.List;

public class StraightEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        cards.sort(Comparator.comparingInt(Card::getCardValueOnPoker));

        boolean isStraightFlush = true;

        for (int j = 1; j < cards.size(); j++) {
            if (cards.get(j).getCardValueOnPoker() != cards.get(j - 1).getCardValueOnPoker() + 1) {
                isStraightFlush = false;
                break;
            }
        }

        if (isStraightFlush) {
            return 9000;
        }

        return 0;
    }
}
