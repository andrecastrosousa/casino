package academy.mindswap.p1g2.casino.server.utils;

import academy.mindswap.p1g2.casino.server.games.slotMachine.SlotMachine;

public class Messages {

    public static final String SEPARATOR = "-----------------------------------";
    public static final String USER_SEPARATOR = "------------- USERS ---------------\n";
    public static final String COMMAND_SEPARATOR = "------------ COMMANDS --------------\n";
    public static final String POKER_SEPARATOR = "------------- POKER ---------------\n";
    public static final String SERVER_RUNNING = "Server is running";
    public static final String SERVER_OPENING = "Server is accepting Clients";
    public static final String ROOM_WAITING = "\nWaiting for players to start the game...\n";
    public static final String ROOM_STARTED = "Game will start soon...";
    public static final String CLIENT_MESSAGE = "client sent: %s\n";
    public static final String CLIENT_ARRIVED = "New client arrived";
    public static final String CLIENT_WELCOME = "Welcome to our chat!";
    public static final String CLIENT_NAME = "Your name is %s";
    public static final String INSERT_COMMAND = "Insert a command";
    public static final String WELCOME = "Welcome to blackjack game!!!";
    public static final String START = "Let´s start!!!";
    public static final String PLAYER = "Player: ";
    public static final String DEALER = "Dealer: ";

    // SLOT MACHINE MESSAGES
    public static final String JACKPOT = "***JACKPOT TIME BABY***";
    public static final String HALF_JACKPOT = "***HALF-JACKPOT***";
    public static final String LOSE_SLOT_MACHINE = "Sorry, you didn't win anything... :("; // Can be standardized for all!
    public static final String THREE_MATCHES = "***YOU GOT THREE MATCHES***";
    public static final String TWO_MATCHES = "***YOU GOT TWO MATCHES***";
    public static final String SLOT_MACHINE_WELCOME = "Welcome to the Slot Machine!";
    public static final String SLOT_MACHINE_SPIN = "Press Enter to spin the reels (or type 'quit' to exit):";
    public static final String SLOT_MACHINE_DOUBLE = "Type 'double' to double your bet!";
    public static final String SLOT_MACHINE_END_BALANCE = "You finished the game with %d credits!";
    public static final String SLOT_MACHINE_GOODBYE = "Thank you for playing. Goodbye!";
    public static final String SLOT_MACHINE_NO_CREDITS = "Insufficient credits to place the bet. Please try again.";
    public static final String SLOT_MACHINE_BALANCE_WIN = "Balance: %d";;
    public static final String SLOT_MACHINE_CONGRATULATIONS = "Congratulations! You won %d credits! :D";
    public static final String SLOT_MACHINE_BALANCE = "Your balance is: %d";
    public static final String SLOT_MACHINE_GAMEOVER = "Game over. :(";
    public static final String SLOT_MACHINE_SEPARATOR = "\n--------*--------";
    public static final String SLOT_MACHINE_NRS_DISPLAY = "| %d |";;






    /* CARDS */
    public static final String CARD_HEART = """
              ______
             |%s     |
             | (\\/) |
             |  \\/  |
             |     %s|
             `------`
            """;

    public static final String CARD_DIAMOND = """
              ______
             |%s     |
             | / \\  |
             | \\ /  |
             |     %s|
             `------`
            """;

    public static final String CARD_SPADE = """
              ______
             |%s     |
             | / \\  |
             |(___) |
             | / \\ %s|
             `------`
            """;

    public static final String CARD_CLUB = """
              ______
             |%s     |
             |  &   |
             | &|&  |
             |  |  %s|
             `------´
            """;

    private Messages() {}
}
