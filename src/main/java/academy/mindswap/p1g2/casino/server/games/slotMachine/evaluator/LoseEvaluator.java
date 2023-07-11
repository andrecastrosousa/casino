package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;

public class LoseEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    private PlaySound loseSound;

    public LoseEvaluator() {
        loseSound = new PlaySound("../casino/sounds/lose_sound.wav");
    }
    @Override
    public boolean evaluateHand(int payout, Player player) throws IOException {
        if (payout == 0) {
            playLoseSound();
            player.sendMessage(Messages.LOSE_SLOT_MACHINE);
            return true;
        }
        return evaluateNext(payout, player);
    }
    private void playLoseSound() {
        loseSound.play();
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
