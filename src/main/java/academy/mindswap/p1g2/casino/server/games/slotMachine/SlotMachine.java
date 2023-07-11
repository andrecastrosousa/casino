package academy.mindswap.p1g2.casino.server.games.slotMachine;
import academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator.*;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.*;

public class SlotMachine {
    private static final int spinsCount = 3; // Number of spins
    private static final Random random = new Random();
    int bet;
    private Evaluator evaluatorChain;
    Player player;
    private PlaySound spinSound;

    public SlotMachine() {

        evaluatorChain = new LoseEvaluator();
        Evaluator evaluatorJackpot = new JackpotEvaluator();
        Evaluator evaluatorHalfJackpot = new HalfJackpotEvaluator();
        evaluatorChain.setNextEvaluator(evaluatorJackpot);
        evaluatorJackpot.setNextEvaluator(evaluatorHalfJackpot);
        evaluatorHalfJackpot.setNextEvaluator(new TwoMatchEvaluator());
        spinSound = new PlaySound("../casino/sounds/spin_sound.wav");


    }

    public void play() throws IOException, InterruptedException {
        player.sendMessage(Messages.SLOT_MACHINE_WELCOME);

        Thread.sleep(5000);


            /*player.sendMessage(Messages.SLOT_MACHINE_SPIN);
            player.sendMessage(Messages.SLOT_MACHINE_DOUBLE);


            if (input.equalsIgnoreCase("quit")) { //
                player.sendMessage(String.format(Messages.SLOT_MACHINE_END_BALANCE, balance));
                player.sendMessage(Messages.SLOT_MACHINE_GOODBYE);
                break;
            }*/
            if (player.getBalance() < bet) {
                player.sendMessage(Messages.SLOT_MACHINE_NO_CREDITS);
            }
            player.addBalance( - bet);
            playSpinSound();
            Thread.sleep(2000);
            List<Integer> spins = spin();

            displaySpins(spins);
            int payout = calculatePayout(spins, 2, 3);
            evaluatorChain.evaluateHand(payout, player);
            player.addBalance(payout);
        if (payout > 0) {
            player.addBalance(payout);
            player.sendMessage(String.format(Messages.SLOT_MACHINE_BALANCE_WIN, (player.getBalance() - payout)));
            player.sendMessage(String.format(Messages.SLOT_MACHINE_CONGRATULATIONS, payout));
        }

        player.sendMessage(String.format(Messages.SLOT_MACHINE_BALANCE, player.getBalance()));
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
        player.sendMessage(Messages.SLOT_MACHINE_SEPARATOR);
        player.sendMessage(String.format(Messages.SLOT_MACHINE_NRS_DISPLAY, spins.get(0), spins.get(1), spins.get(2)));
        player.sendMessage(Messages.SLOT_MACHINE_SEPARATOR);
    }

    /**
     * hasdjasdha
     *
     * @param spins             number of spin made by user.
     * @param minimumMatchPrize minimum match to win (two numbers)
     * @param maximumMatchPrize maximum match to win (three numbers)
     * @return
     */
    private int calculatePayout(List<Integer> spins, int minimumMatchPrize, int maximumMatchPrize) { //{2,3,3,4}   {2:!,3:2,4:1}
        Map<Integer, Integer> matrix = new HashMap<>();
        for (Integer num : spins) {
            matrix.put(num, matrix.get(num) != null ? matrix.get(num) + 1 : 1);
        }

        if (matrix.containsValue(minimumMatchPrize)) {
            return 3 * bet;
        } else if (matrix.containsValue(maximumMatchPrize)) {
            if (spins.contains(7) && matrix.get(7) == spins.size()) {
                return 20 * bet;
            } else if (spins.contains(3) && matrix.get(3) == spins.size()) {
                return 7 * bet;
            }
            return 5 * bet;
        }
        return 0;
    }

    public void sitPlayer(Player player) {
        this.player = player;
    }
    public void setBet(int bet){
        this.bet = bet;
    }
    private void playSpinSound() {
        spinSound.play();
    }


}



