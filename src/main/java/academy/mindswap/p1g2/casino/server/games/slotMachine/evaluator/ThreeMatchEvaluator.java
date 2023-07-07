package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

public class ThreeMatchEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    @Override
    public boolean evaluateHand(int payout) {
        if (payout % 5 == 0) {
            System.out.println("***YOU GOT THREE MATCHES***");
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

