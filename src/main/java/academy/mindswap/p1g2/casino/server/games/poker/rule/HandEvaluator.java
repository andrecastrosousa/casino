package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.List;

public abstract class HandEvaluator implements Evaluator {
    private Evaluator nextEvaluator;

    @Override
    public abstract HandScore evaluateHand(List<Card> cards);

    @Override
    public HandScore evaluateNext(List<Card> cards) {
        if(nextEvaluator != null) {
            return nextEvaluator.evaluateHand(cards);
        }
        return null;
    }

    public static Evaluator getEvaluatorChain() {
        Evaluator evaluatorChain = new RoyalFlushEvaluator();
        Evaluator straightFlushEvaluator = new StraightFlushEvaluator();
        evaluatorChain.setNextEvaluator(straightFlushEvaluator);
        Evaluator fourOfAKindEvaluator = new FourOfAKindEvaluator();
        straightFlushEvaluator.setNextEvaluator(fourOfAKindEvaluator);
        Evaluator fullHouseEvaluator = new FullHouseEvaluator();
        fourOfAKindEvaluator.setNextEvaluator(fullHouseEvaluator);

        Evaluator flushEvaluator = new FlushEvaluator();
        fullHouseEvaluator.setNextEvaluator(flushEvaluator);

        Evaluator straightEvaluator = new StraightEvaluator();
        flushEvaluator.setNextEvaluator(straightEvaluator);
        Evaluator threeOfKindEvaluator = new ThreeOfAKindEvaluator();
        straightEvaluator.setNextEvaluator(threeOfKindEvaluator);
        Evaluator twoPairEvaluator = new TwoPairEvaluator();
        threeOfKindEvaluator.setNextEvaluator(twoPairEvaluator);
        Evaluator onePairEvaluator = new OnePairEvaluator();
        twoPairEvaluator.setNextEvaluator(onePairEvaluator);
        Evaluator highCardEvaluator = new HighCardEvaluator();
        onePairEvaluator.setNextEvaluator(highCardEvaluator);

        return evaluatorChain;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;
    }
}
