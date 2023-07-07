package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public abstract class HandEvaluator implements Evaluator {
    private Evaluator nextEvaluator;

    @Override
    public abstract boolean evaluateHand();

    @Override
    public boolean evaluateNext(List<Card> cards) {
        if(nextEvaluator != null) {
            return nextEvaluator.evaluateHand();
        }
        return false;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;
    }
}
