package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class FourOfAKindEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = getPairCards(cards, 4);

        if(newHand.size() == 4) {
            List<Card> higherCards = getHigherCards(cards, newHand);

            newHand.add(higherCards.get(0));
            return new HandScore(EvaluatorType.FOUR_OF_KIND.getScore(), newHand, EvaluatorType.FOUR_OF_KIND);
        }

        return evaluateNext(cards);
    }
}
