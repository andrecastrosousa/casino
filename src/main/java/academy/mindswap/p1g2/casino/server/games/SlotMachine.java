package academy.mindswap.p1g2.casino.server.games;

import java.util.*;


public class SlotMachine {
    private static final int spinsCount = 3; // Number of spins
    private static final int initialBalance = 20; // Initial balance for player (can be another nr)
    private static final Scanner scanner = new Scanner(System.in); // Create a Scanner
    private static final Random random = new Random();

    private int balance;
    int bet;

    public SlotMachine() {
        balance = initialBalance;
    }

    public void play() {
        System.out.println("Welcome to the Slot Machine!");
        while (balance > 0) {

            System.out.println("Press Enter to spin the reels (or type 'quit' to exit):");
            System.out.println("Press 'double' to double your bet!");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) { //
                System.out.println("Thank you for playing. Goodbye!");
                break;
            }

            if(input.equalsIgnoreCase("double")) {
                bet = 2;
            } else {
                bet = 1;
            }
            if (balance < bet) {
                System.out.println("Insufficient credits to place the bet. Please try again.");
                continue;
            }
            balance -= bet;




            List<Integer> spins = spin();
            displaySpins(spins);
            int payout = calculatePayout(spins, 2, 3);
            if (payout == 20 || payout == 40 ){
                System.out.println("***JACKPOT TIME BABY***");
            } if(payout == 7 || payout == 14){
                System.out.println("***HALF-JACKPOT***");
            }
            if(payout == 3 || payout == 6){
                System.out.println("***YOU GOT TWO MATCHES***");
            }
            if (payout > 0) {
                if (bet == 2) {
                    payout *= 2; // Double the payout if the player wins with the double bet
                }
                balance += payout; // Update the balance with the payout
                System.out.println("Balance: " + (balance - payout));
                System.out.println("Congratulations! You won " + payout + " credits! :D");

            } else {
                System.out.println("Sorry, you didn't win anything... :(");
            }

            System.out.println("Your balance is: " + balance);
        }

        System.out.println("Game over. You are out of credits... :(");
    }

    private List<Integer> spin() {
        List<Integer> spins = new ArrayList<>();

        for (int i = 0; i < spinsCount; i++) {
            int number = random.nextInt(9); // Generate a random number between 1 and 9
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
     * @param spins number of spin made by user.
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
            if (bet ==2){
                return 3;
            } else {
                return (int) (1.5+1.5);
            }
        }
        if (matrix.containsValue(maximumMatchPrize)) {
            if (spins.contains(7) && matrix.get(7) == spins.size()) {
                if (bet == 2) {
                    return 20; // Double the jackpot value (40 credits)
                } else {
                    return 10+10; // Jackpot (20 credits)
                }
            } else if (spins.contains(3) && matrix.get(3) == spins.size()) {
                if (bet == 2) {
                    return 7;
                    // Double the half-jackpot value (14 credits)
                } else {
                    return (int) (3.5 + 3.5); // Half-jackpot (7 credits)
                }
            }
            if (bet == 2) {
                return 1; // Double the regular payout value (2 credits)
            } else {
                return (int) (0.5+0.5); // Regular payout (1 credits)
            }
        }
        return 0;
    }
}

