package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class ThreeOfAKindEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = getPairCards(cards, 3);

        if(newHand.size() == 3) {
            List<Card> higherCards = getHigherCards(cards, newHand);
            newHand.addAll(higherCards.subList(0, 2));
            return new HandScore(
                    EvaluatorType.THREE_OF_KIND.getScore(),
                    newHand,
                    EvaluatorType.THREE_OF_KIND
            );
        }

        return evaluateNext(cards);
    }
}
