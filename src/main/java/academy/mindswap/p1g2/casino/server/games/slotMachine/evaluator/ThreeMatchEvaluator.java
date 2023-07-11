package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;

public class ThreeMatchEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    private PlaySound cashSound;
    public ThreeMatchEvaluator() {
        cashSound = new PlaySound("../casino/sounds/cash_sound.wav");
    }

    @Override
    public boolean evaluateHand(int payout, Player player) throws IOException {
        if (payout % 5 == 0) {
            playCashSound();
            player.sendMessage(Messages.THREE_MATCHES);
            return true;
        }
        return evaluateNext(payout, player);
    }
    private void playCashSound() {
        cashSound.play();
    }

    private boolean evaluateNext(int payout, Player player) throws IOException {
        if(nextEvaluator != null){
            return nextEvaluator.evaluateHand(payout, player);
        }
        return false;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;

    }
}

