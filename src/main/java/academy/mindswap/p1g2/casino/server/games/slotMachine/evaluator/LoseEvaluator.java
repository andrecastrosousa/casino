package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

public class LoseEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    @Override
    public boolean evaluateHand(int payout) {
        if (payout == 0) {
            System.out.println("Sorry, you didn't win anything... :(");
            return true;
        }
        return evaluateNext(payout);
    }

    private boolean evaluateNext(int payout) {
        if(nextEvaluator != null){
            return nextEvaluator.evaluateHand(payout);
        }
        return false;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;

    }
}
