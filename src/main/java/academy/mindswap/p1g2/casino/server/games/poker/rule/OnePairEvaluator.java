package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class OnePairEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = getPairCards(cards, 2);;

        if(newHand.size() == 2) {
            List<Card> higherCards = getHigherCards(cards, newHand);
            newHand.addAll(higherCards.subList(0, 3));
            return new HandScore(2000, newHand);
        }

        return evaluateNext(cards);
    }
}
