package academy.mindswap.p1g2.casino.server.games.poker.rule;

public class EvaluatorFactory {

    private EvaluatorFactory() {

    }

    public static Evaluator make() {
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
}
