package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.games.slotMachine.evaluator.*;

import java.util.*;


public class SlotMachine {
    private static final int spinsCount = 3; // Number of spins
    private static final int initialBalance = 20; // Initial balance for player (can be another nr)
    private static final Scanner scanner = new Scanner(System.in); // Create a Scanner
    private static final Random random = new Random();

    private int balance;
    int bet;
    private Evaluator evaluatorChain;

    public SlotMachine() {
        balance = initialBalance;
        evaluatorChain = new LoseEvaluator();
        Evaluator evaluatorJackpot = new JackpotEvaluator();
        Evaluator evaluatorHalfJackpot = new HalfJackpotEvaluator();
        evaluatorChain.setNextEvaluator(evaluatorJackpot);
        evaluatorJackpot.setNextEvaluator(evaluatorHalfJackpot);
        evaluatorHalfJackpot.setNextEvaluator(new TwoMatchEvaluator());
    }

    public void play() {
        System.out.println("Welcome to the Slot Machine!");
        while (balance > 0) {

            System.out.println("Press Enter to spin the reels (or type 'quit' to exit):");
            System.out.println("Type 'double' to double your bet!");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) { //
                System.out.println("You finished the game with " + balance + " credits!");
                System.out.println("Thank you for playing. Goodbye!");
                break;
            }

            if (input.equalsIgnoreCase("double")) {
                bet = 2;
            } else {
                bet = 1;
            }
            if (balance < bet) {
                System.out.println("Insufficient credits to place the bet. Please try again.");
                break;
            }
            balance -= bet;


            List<Integer> spins = spin();
            displaySpins(spins);
            int payout = calculatePayout(spins, 2, 3);
            evaluatorChain.evaluateHand(payout);
            if (payout > 0) {
                balance += payout; // Update the balance with the payout
                System.out.println("Balance: " + (balance - payout));
                System.out.println("Congratulations! You won " + payout + " credits! :D");
            }
            System.out.println("Your balance is: " + balance);
        }
        System.out.println("Game over. :(");
    }

    private List<Integer> spin() {
        List<Integer> spins = new ArrayList<>();

        for (int i = 0; i < spinsCount; i++) {
            int number = random.nextInt(9); // Generate a random number between 0 and 9
            spins.add(number);
        }

        return spins;


    }

    private void displaySpins(List<Integer> spins) {
        System.out.println("\n--------*--------");
        for (int number : spins) {
            System.out.print("| " + number + " | ");
        }
        System.out.println("\n--------*--------");
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
                // Double the half-jackpot value (14 credits)
            }
            return 5 * bet; // Double the regular payout value (2 credits)
        }
        return 0;
    }
}



