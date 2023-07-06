package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.poker.rule.*;

public class Poker {
    private Evaluator evaluatorChain;

    public Poker() {
        evaluatorChain = new RoyalFlushEvaluator();
        evaluatorChain.setNextEvaluator(new StraightFlushEvaluator());
        evaluatorChain.setNextEvaluator(new FourOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new FullHouseEvaluator());
        evaluatorChain.setNextEvaluator(new FlushEvaluator());
        evaluatorChain.setNextEvaluator(new StraightEvaluator());
        evaluatorChain.setNextEvaluator(new ThreeOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new TwoPairEvaluator());
        evaluatorChain.setNextEvaluator(new OnePairEvaluator());
        evaluatorChain.setNextEvaluator(new HighCardEvaluator());
    }
}
