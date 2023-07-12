package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;

public class HalfJackpotEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    private final PlaySound halfJackpotSound;

    public HalfJackpotEvaluator() {
        halfJackpotSound = new PlaySound("../casino/sounds/half_jackpot_sound.wav");
    }

    @Override
    public boolean evaluateHand(int payout, Player player) throws IOException {
        if (payout % 7 == 0) {
            playHalfJackpotSound();
            player.sendMessage(Messages.HALF_JACKPOT);
            return true;
        }
        return evaluateNext(payout, player);
    }

    private void playHalfJackpotSound() {
        halfJackpotSound.play();
    }


    private boolean evaluateNext(int payout, Player player) throws IOException {
        if (nextEvaluator != null) {
            return nextEvaluator.evaluateHand(payout, player);
        }
        return false;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;

    }
}
