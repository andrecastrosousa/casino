package academy.mindswap.p1g2.casino.server.games.slotMachine.manager;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator.Evaluator;
import academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator.SlotEvaluatorFactory;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.*;

public class SlotMachine {
    private static final int spinsCount = 3; // Number of spins
    private static final int MINIMUM_MATCH_PRIZE = 2;
    private static final int MAXIMUM_MATCH_PRIZE = 3;
    private static final Random random = new Random();
    private final Evaluator evaluatorChain;
    private final PlaySound spinSound;
    int bet;
    Player player;

    public SlotMachine() {
        evaluatorChain = SlotEvaluatorFactory.make();
        spinSound = new PlaySound("../casino/sounds/spin_sound.wav");
    }

    private List<Integer> spin() {
        List<Integer> spins = new ArrayList<>();
        for (int i = 0; i < spinsCount; i++) {
            int number = random.nextInt(9); // Generate a random number between 0 and 9. Increase the occurrence of certain numbers to reduce the odds.
            spins.add(number);
        }
        return spins;
    }

    private void displaySpins(List<Integer> spins) throws IOException {
        player.sendMessage(String.format(Messages.SLOT_MACHINE, spins.get(0), spins.get(1), spins.get(2)));
    }

    public void play() throws IOException, InterruptedException {

        if (player.getCurrentBalance() < bet) {
            player.sendMessage(Messages.SLOT_MACHINE_NO_CREDITS);
        }

        playSpinSound();
        Thread.sleep(2000);
        List<Integer> spins = spin();

        displaySpins(spins);
        calculatePayout(spins);
    }

    private void calculatePayout(List<Integer> spins) throws IOException { //{2,3,3,4}   {2:!,3:2,4:1}
        Map<Integer, Integer> matrix = new HashMap<>();
        int payout = 0;

        for (Integer num : spins) {
            int count = matrix.getOrDefault(num, 0);
            matrix.put(num, count + 1);
        }

        if (matrix.containsValue(MINIMUM_MATCH_PRIZE)) {
            payout = 3 * bet;
        } else if (matrix.containsValue(MAXIMUM_MATCH_PRIZE)) {
            if (spins.contains(7) && matrix.get(7) == spins.size()) {
                payout = 20 * bet;
            } else if (spins.contains(3) && matrix.get(3) == spins.size()) {
                payout = 7 * bet;
            } else {
                payout = 5 * bet;
            }
        }
        evaluatePayout(payout);
    }

    private void evaluatePayout(int payout) throws IOException {
        evaluatorChain.evaluateHand(payout, player);
        player.addBalance(payout - bet);
        if (payout > 0) {
            player.sendMessage(String.format(Messages.SLOT_MACHINE_BALANCE_WIN, (player.getCurrentBalance() - payout)));
            player.sendMessage(String.format(Messages.SLOT_MACHINE_CONGRATULATIONS, payout));
        }

        player.sendMessage(String.format(Messages.SLOT_MACHINE_BALANCE, player.getCurrentBalance()));
    }

    public void sitPlayer(Player player) {
        this.player = player;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    private void playSpinSound() {
        spinSound.play();
    }


}



