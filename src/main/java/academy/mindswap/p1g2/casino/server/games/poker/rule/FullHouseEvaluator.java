package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.participant.HandScore;

import java.util.ArrayList;
import java.util.List;

public class FullHouseEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<List<Card>> cardsPair = getMultiplePairCards(cards);

        if (cardsPair.size() >= 2 && (cardsPair.get(0).size() == 3 || cardsPair.get(1).size() == 3)) {
            List<Card> newHand = new ArrayList<>();
            newHand.addAll(cardsPair.get(0));
            newHand.addAll(cardsPair.get(1));

            return new HandScore(EvaluatorType.FULL_HOUSE.getScore(), newHand, EvaluatorType.FULL_HOUSE);
        }

        return evaluateNext(cards);
    }
}
