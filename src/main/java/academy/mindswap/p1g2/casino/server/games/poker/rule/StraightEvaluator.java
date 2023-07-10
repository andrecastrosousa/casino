package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class StraightEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> pairsToRemove = getPairCards(cards, 2);

        List<Card> cardsWithoutPairs = new ArrayList<>(cards);
        if (pairsToRemove.size() >= 2) {
            cardsWithoutPairs.remove(pairsToRemove.get(0));
        }

        int indexOfLastCombinationCard = isStraight(cardsWithoutPairs);

        if (indexOfLastCombinationCard > 0) {
            return new HandScore(5000, cardsWithoutPairs.subList(indexOfLastCombinationCard - 4, indexOfLastCombinationCard + 1));
        }

        return evaluateNext(cards);
    }
}
