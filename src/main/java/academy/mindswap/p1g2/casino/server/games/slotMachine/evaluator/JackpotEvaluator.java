package academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator;

import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;

public class JackpotEvaluator implements Evaluator {
    private Evaluator nextEvaluator;
    private PlaySound jackpotSound;
    public JackpotEvaluator() {
        jackpotSound = new PlaySound("../casino/sounds/jackpot_sound.wav");
    }
    @Override
    public boolean evaluateHand(int payout, Player player) throws IOException {
        if (payout % 20 == 0) {
            playJackpotSound();
            player.sendMessage(Messages.JACKPOT);
            return true;
        }
        return evaluateNext(payout, player);
    }
    private void playJackpotSound() {
        jackpotSound.play();
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
