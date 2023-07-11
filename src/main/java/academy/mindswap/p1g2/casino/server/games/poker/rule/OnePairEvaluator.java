package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class OnePairEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = getPairCards(cards, 2);;

        if(newHand.size() == 2) {
            List<Card> higherCards = getHigherCards(cards, newHand);
            newHand.addAll(higherCards.subList(0, 3));
            return new HandScore(EvaluatorType.ONE_PAIR.getScore(), newHand, EvaluatorType.ONE_PAIR);
        }

        return evaluateNext(cards);
    }
}
