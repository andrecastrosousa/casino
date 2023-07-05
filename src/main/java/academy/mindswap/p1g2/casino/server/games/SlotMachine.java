package academy.mindswap.p1g2.casino.server.games;

import java.util.*;


public class SlotMachine {
    private static final int spinsCount = 3; // Number of spins
    private static final int numbersPerSpin = 3; // Number of number for each spin
    private static final int initialBalance = 100; // Initial balance for player (can be another nr)
    private static final Scanner scanner = new Scanner(System.in); // Create a Scanner
    private static final Random random = new Random();

    private int balance;

    public SlotMachine() {
        balance = initialBalance;
    }

    public void play() {
        System.out.println("Welcome to the Slot Machine!");
        while (balance > 0) {

            System.out.println("Press Enter to spin the reels (or type 'quit' to exit):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) { //
                System.out.println("Thank you for playing. Goodbye!");
                break;
            }
            balance -= 1;
            /*if (input.equalsIgnoreCase("double")) {  // Double the bet!
                System.out.println("You double the bet!");
            }

           Podia adicionar uma aposta a dobrar!!
            */



            List<Integer> spins = spin();
            displaySpins(spins);
            int payout = calculatePayout(spins, 2, 3);
            if (payout > 0) {
                System.out.println("Balance: " + balance);
                System.out.println("Congratulations! You won " + payout + " credits! :D");
            } else {
                System.out.println("Sorry, you didn't win anything... :(");
            }
            int balanceUpdate = balance + payout;
            System.out.println("Your balance is: " + balanceUpdate);
        }

        System.out.println("Game over. You are out of credits... :(");
    }

    private List<Integer> spin() {
        List<Integer> spins = new ArrayList<>();

        for (int i = 0; i < spinsCount; i++) {
            int number = random.nextInt(9) + 1; // Generate a random number between 1 and 9
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

    private int calculatePayout(List<Integer> spins, int minimumMatchPrize, int maximumMatchPrize) { //{2,3,3,4}   {2:!,3:2,4:1}
        Map<Integer, Integer> matrix = new HashMap<>();
        for (Integer num : spins) {
            matrix.put(num, matrix.get(num) != null ? matrix.get(num) + 1 : 1);
        }

        if (matrix.containsValue(2)) {
            return 3;
        }
        if (matrix.containsValue(3)) {
            return 10;
        }
        return 0;
    }
}



