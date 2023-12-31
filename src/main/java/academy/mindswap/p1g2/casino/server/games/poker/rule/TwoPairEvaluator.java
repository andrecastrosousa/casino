package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.participant.HandScore;

import java.util.ArrayList;
import java.util.List;

public class TwoPairEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<List<Card>> cardsPair = getMultiplePairCards(cards);

        if (cardsPair.size() == 2) {
            List<Card> newHand = new ArrayList<>();
            newHand.addAll(cardsPair.get(0));
            newHand.addAll(cardsPair.get(1));

            List<Card> higherCards = getHigherCards(cards, newHand);

            newHand.add(higherCards.get(0));

            return new HandScore(EvaluatorType.TWO_PAIR.getScore(), newHand, EvaluatorType.TWO_PAIR);
        }

        return evaluateNext(cards);
    }
}
