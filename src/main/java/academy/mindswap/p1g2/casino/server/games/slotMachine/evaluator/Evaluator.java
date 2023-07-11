package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;

public interface Evaluator {
    boolean evaluateHand(int payout, Player player) throws IOException;
    void setNextEvaluator(Evaluator evaluator);
}
