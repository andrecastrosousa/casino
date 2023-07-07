package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

public interface Evaluator {
    boolean evaluateHand(int payout);
    void setNextEvaluator(Evaluator evaluator);
}
