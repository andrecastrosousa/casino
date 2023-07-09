package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public abstract class HandEvaluator implements Evaluator {
    private Evaluator nextEvaluator;

    @Override
    public abstract int evaluateHand(List<Card> cards);

    @Override
    public int evaluateNext(List<Card> cards) {
        if(nextEvaluator != null) {
            return nextEvaluator.evaluateHand(cards);
        }
        return 0;
    }

    public static Evaluator getEvaluatorChain() {
        Evaluator evaluatorChain = new RoyalFlushEvaluator();
        Evaluator straightFlushEvaluator = new StraightEvaluator();
        evaluatorChain.setNextEvaluator(straightFlushEvaluator);
        Evaluator fourOfAKindEvaluator = new FourOfAKindEvaluator();
        straightFlushEvaluator.setNextEvaluator(fourOfAKindEvaluator);
        Evaluator fullHouseEvaluator = new FullHouseEvaluator();
        fourOfAKindEvaluator.setNextEvaluator(fullHouseEvaluator);
        Evaluator straightEvaluator = new StraightEvaluator();
        fullHouseEvaluator.setNextEvaluator(straightEvaluator);
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
