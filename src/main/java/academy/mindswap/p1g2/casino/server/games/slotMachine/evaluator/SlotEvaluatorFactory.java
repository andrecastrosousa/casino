package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

public class SlotEvaluatorFactory {
    private SlotEvaluatorFactory() {

    }

    public static Evaluator make() {
        Evaluator evaluatorChain = new LoseEvaluator();
        Evaluator evaluatorJackpot = new JackpotEvaluator();
        Evaluator evaluatorHalfJackpot = new HalfJackpotEvaluator();
        evaluatorChain.setNextEvaluator(evaluatorJackpot);
        evaluatorJackpot.setNextEvaluator(evaluatorHalfJackpot);
        evaluatorHalfJackpot.setNextEvaluator(new TwoMatchEvaluator());

        return evaluatorChain;
    }
}
