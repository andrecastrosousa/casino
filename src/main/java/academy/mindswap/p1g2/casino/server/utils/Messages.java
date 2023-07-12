package academy.mindswap.p1g2.casino.server.utils;


public class Messages {

    public static final String SEPARATOR = "-----------------------------------";
    public static final String USER_SEPARATOR = "------------- USERS ---------------\n";
    public static final String COMMAND_SEPARATOR = "------------ COMMANDS --------------\n";
    public static final String POKER_SEPARATOR = "------------- POKER ---------------\n";
    public static final String SERVER_RUNNING = "Server is running";
    public static final String SERVER_OPENING = "Server is accepting Clients";
    public static final String ROOM_WAITING = "\nYou've entered in room %d.\nWait for other players to start the game...\n";
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

    // SLOT MACHINE MESSAGES--------------------------------------------------------------------------------------------
    public static final String JACKPOT = "\033[0;33m***JACKPOT TIME BABY*** \033[0m ";
    public static final String HALF_JACKPOT = "\033[0;33m***HALF-JACKPOT***\033[0m ";
    public static final String LOSE_SLOT_MACHINE = "\033[1;31mSorry, you didn't win anything... :( \033[1m ";
    public static final String THREE_MATCHES = "\033[0;33m***YOU GOT THREE MATCHES***\033[0m";
    public static final String TWO_MATCHES = "\033[0;33m***YOU GOT TWO MATCHES***\033[0m";
    public static final String SLOT_MACHINE_WELCOME = "\033[0;36mWelcome to the Slot Machine!\033[0m";
    public static final String SLOT_MACHINE_END_BALANCE = "You finished the game with \033[0;37m%d\033[0;37m credits!";
    public static final String SLOT_MACHINE_GOODBYE = "Thank you for playing. Goodbye!";
    public static final String SLOT_MACHINE_NO_CREDITS = "Insufficient credits to place the bet. Please try again.";
    public static final String SLOT_MACHINE_BALANCE_WIN = "\033[1;37mBalance:\033[1m \033[0;34m%d\033[0m";
    public static final String SLOT_MACHINE_CONGRATULATIONS = "\033[1;32mCongratulations!\033[0m" + " You won \033[0;32m%d\033[0m credits! :D";
    public static final String SLOT_MACHINE_BALANCE = "\033[1;37mYour balance is:\033[1m \033[0;35m%d\033[0m";
    public static final String SLOT_MACHINE_GAMEOVER = "\033[1;31mGame over.\033[1m :(";
    public static final String SLOT_MACHINE_SEPARATOR = "\033[1;36m--------*--------\033[1m";
    public static final String SLOT_MACHINE_NRS_DISPLAY = "\033[1;36m| %d | | %d | | %d |\033[1m";

    // New messages-----------------------------------------------------------------------------------------------------
    public static final String NOT_YOUR_TURN = "Isn't your time to play.";
    public static final String YOUR_TURN = "It is \033[1;34myour\033[0m time to play.";
    public static final String SOMEONE_PLAYING = "\033[1;34m%s\033[0m is playing. Wait for your turn.";
    public static final String WINNER = "%s win the game!";
    public static final String YOU_WON = "You won the game!";

    //Poker messages----------------------------------------------------------------------------------------------------
    public static final String SOMEONE_ALL_IN = "%s did an all in";
    public static final String YOU_ALL_IN = "You did an all in";
    public static final String YOU_ALL_IN_W_CHIPS = "%s did an all in with %d poker chips";
    public static final String NO_BET_CANT_CALL = "\033[1;31mYou can't call because no one bet. You only can /bet, /check or /fold.\033[0m";
    public static final String HIGHER_BET_CANT_CALL = "\033[1;31mYou can't call because you have the higher bet. You only can /check or /fold.\033[0m";
    public static final String SOMEONE_ALL_IN_W_CHIPS = "%s did an all in with %d poker chips";
    public static final String SOMEONE_CALL_W_CHIPS = "%s did a call with %d poker chips";
    public static final String YOU_ALL_IN_W_CHIPS_BALANCE = "You did an all in with %d poker chips, your current balance is %d";
    public static final String YOU_CALL_W_CHIPS_BALANCE = "You did a call with %d poker chips, your current balance is %d";
    public static final String SOMEONE_CHECK = "%s did a check.";
    public static final String YOU_CHECK = "You did a check";
    public static final String HIGHER_BET_CANT_CHECK = "\033[1;31mYou can't check because someone bet higher. You only can /call, /fold, /allIn or /raise.\033[0m";
    public static final String SOMEONE_FOLD = "%s did a fold.";
    public static final String YOU_FOLD = "You did a fold";
    public static final String NO_ONE_BET_CANT_RISE = "\033[1;31mYou can't raise because no one bet. You only can /bet, /fold or /allIn\033[0m";
    public static final String YOU_RISE_W_CHIPS = "%s did a raise with %d poker chips";
    public static final String YOU_RISE_W_CHIPS_BALANCE = "You did a raise with %d poker chips, your current balance is %d";
    public static final String SOMEONE_BET_20 = "%s did a bet with 20 poker chips";
    public static final String YOU_BET_20 = "You did a bet with 20 poker chips, your current balance is %d";
    public static final String CURRENT_BALANCE = "Current balance %d";
    public static final String CANT_BET_SOMEONE_BET = "\033[1;31mYou can't bet because someone already did it. You only can /call, /raise, /allIn or /fold\033[0m";
    public static final String YOU_WON_HAND = "You won the hand with %s and won %d poker chips";
    public static final String SOMEONE_WON_HAND = "%s won the hand and with %s won %d poker chips";
    public static final String YOU_LOSE_ALL_CHIPS = "You have lost all your poker chips. Thanks for playing.";
    public static final String SOMEONE_WON_HAND_W = "%s won the hand with %s and won %d poker chips.";

    //Blackjack messages------------------------------------------------------------------------------------------------
    public static final String HAND_BURST = "Your hand burst!";
    public static final String FIRST_HAND_BURST = "Your first hand burst!!!";
    public static final String YOU_GIVE_UP = "You give up!";
    public static final String YOU_WIN = "You win!";
    public static final String DEALER_WIN = "Dealer wins this round!";
    public static final String DEALER_HAND = "Dealer hand: \n";

    /* CARDS */
    public static final String SUIT_HEART = "\033[1;31m♥\033[0m";

    public static final String SUIT_DIAMOND = "\033[1;31m♦\033[0m";

    public static final String SUIT_SPADE = "\033[1;30m♠\033[0m";

    public static final String SUIT_CLUB = "\033[1;30m♣\033[0m";

    //Votes ------------------------------------------------------------------------------------------------------------
    public static final String BLACKJACK_VOTE = "You've voted on Blackjack. \nWaiting for everyone to vote.\n";
    public static final String POKER_VOTE = "You've voted on Poker. \nWaiting for everyone to vote.\n";
    public static final String SLOTS_VOTE = "You've voted on Slots. \nWaiting for everyone to vote.\n";
    public static final String NUMBER_OF_VOTES_SLOTS = "\n%d votes on Slots";
    public static final String NUMBER_OF_VOTES_BLACKJACK = "\n%d votes on Blackjack";
    public static final String NUMBER_OF_VOTES_POKER = "\n%d votes on Poker";
    public static final String VOTE_MESSAGE = "\n\nVote for a game to play...";


    public static final String CARD_BOARD5 = """
             ____________________________________________________________________________________________
            |                                                                                            |           
            |    ´---------`     ´---------`       ´---------`       ´---------`          ´---------`    |                 
            |    |%s        |     |%s        |       |%s        |       |%s        |          |%s        |    |          
            |    |         |     |         |       |         |       |         |          |         |    |
            |    |    %s    |     |    %s    |       |    %s    |       |    %s    |          |    %s    |    |                 
            |    |         |     |         |       |         |       |         |          |         |    |
            |    |        %s|     |        %s|       |        %s|       |        %s|          |        %s|    |
            |     `--------´      `--------´        `--------´        `--------´           `--------´    |                                             
            |____________________________________________________________________________________________|""";

    public static final String CARD_BOARD4 = """
             _______________________________________________________________________
            |                                                                       |          
            |    ´---------`     ´---------`       ´---------`       ´---------`    |                
            |    |%s        |     |%s        |       |%s        |       |%s        |    |          
            |    |         |     |         |       |         |       |         |    |
            |    |    %s    |     |    %s    |       |    %s    |       |    %s    |    |                
            |    |         |     |         |       |         |       |         |    |
            |    |        %s|     |        %s|       |        %s|       |        %s|    |
            |     `--------´      `--------´        `--------´        `--------´    |                                            
            |_______________________________________________________________________|""";
    public static final String CARD_BOARD3 = """
             ____________________________________________________
            |                                                    |          
            |    ´---------`     ´---------`       ´---------`   |                 
            |    |%s        |     |%s        |       |%s        |   |          
            |    |         |     |         |       |         |   |
            |    |    %s    |     |    %s    |       |    %s    |   |               
            |    |         |     |         |       |         |   |
            |    |        %s|     |        %s|       |        %s|   |
            |     `--------´      `--------´        `--------´   |                                             
            |____________________________________________________|""";
    public static final String CARD_BOARD2 = """
             __________________________________
            |                                  |           
            |    ´---------`     ´---------`   |               
            |    |%s        |     |%s        |   |         
            |    |         |     |         |   |
            |    |    %s    |     |    %s    |   |                
            |    |         |     |         |   |
            |    |        %s|     |        %s|   |
            |     `--------´      `--------´   |                                         
            |__________________________________|""";


    public static final String CARD_BOARD1 = """
             _________________
            |                 |       
            |    ´---------`  |               
            |    |%s        |  |        
            |    |         |  |
            |    |    %s    |  |                 
            |    |         |  |
            |    |        %s|  |
            |     `--------´  |                                           
            |_________________|""";


    public static final String SLOT_MACHINE = """
                                          .-------.
                                          |Jackpot|
                              ____________|_______|____________
                             |  __    __    ___  _____   __    | \s
                             | / _\\  / /   /___\\/__   \\ / _\\   |\s
                             | \\ \\  / /   //  //  / /\\ \\\\ \\  25| \s
                             | _\\ \\/ /___/ \\_//  / /  \\/_\\ \\ []|\s
                             | \\__/\\____/\\___/   \\/     \\__/ []|
                             |===_______===_______===_______===|
                             ||*|\\_     |*| _____ |*|\\_     |*||
                             ||*|| \\ _  |*||     ||*|| \\ _  |*||
                             ||*| \\_(_) |*||*BAR*||*| \\_(_) |*||
                             ||*| (_)   |*||_____||*| (_)   |*||  __
                             ||*|_______|*|_______|*|_______|*|| (__)
                             |===_______===_______===_______===|  ||
                             ||*| _____ |*| _____ |*| _____ |*||  ||
                             ||*||     ||*||     ||*||     ||*||  ||
                             ||*||  \033[1;33m%s\033[0m  ||*||  \033[1;33m%s\033[0m  ||*||  \033[1;33m%s\033[0m  ||*||  ||
                             ||*||     ||*||     ||*||     ||*||  ||
                             ||*||_____||*||_____||*||_____||*||_//
                             |===_______===_______===_______===|_/
                             ||*|  ___  |*|   |   |*| _____ |*||
                             ||*| |_  | |*|  / \\  |*||     ||*||
                             ||*|  / /  |*| /_ _\\ |*||*BAR*||*||             \s
                             ||*| /_/   |*|   O   |*||_____||*||       \s
                             ||*|_______|*|_______|*|_______|*||
                             |lc=___________________________===|
                             |  /___________________________\\  |
                             |   |                         |   |
                            _|    \\_______________________/    |_
                           (_____________________________________)
                        
            """;

    public static final String TITLE_SLOT_MACHINE = """
                                    ██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗\s
                                    ██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗
                                    ██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║
                                    ██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║
                                    ╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝
                                     ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝\s
                                     
                                 ███████╗██╗      ██████╗ ████████╗    ███╗   ███╗ █████╗  ██████╗██╗  ██╗██╗███╗   ██╗███████╗
                                 ██╔════╝██║     ██╔═══██╗╚══██╔══╝    ████╗ ████║██╔══██╗██╔════╝██║  ██║██║████╗  ██║██╔════╝
                                 ███████╗██║     ██║   ██║   ██║       ██╔████╔██║███████║██║     ███████║██║██╔██╗ ██║█████╗ \s
                                 ╚════██║██║     ██║   ██║   ██║       ██║╚██╔╝██║██╔══██║██║     ██╔══██║██║██║╚██╗██║██╔══╝ \s
                                 ███████║███████╗╚██████╔╝   ██║       ██║ ╚═╝ ██║██║  ██║╚██████╗██║  ██║██║██║ ╚████║███████╗
                                 ╚══════╝╚══════╝ ╚═════╝    ╚═╝       ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
                                                                                                                              \s
            """;

    public static final String TITLE_POKER = """
                                    ██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗\s
                                    ██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗
                                    ██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║
                                    ██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║
                                    ╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝
                                     ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝\s
                                     
                                                         ██████╗  ██████╗ ██╗  ██╗███████╗██████╗\s
                                                         ██╔══██╗██╔═══██╗██║ ██╔╝██╔════╝██╔══██╗
                                                         ██████╔╝██║   ██║█████╔╝ █████╗  ██████╔╝
                                                         ██╔═══╝ ██║   ██║██╔═██╗ ██╔══╝  ██╔══██╗
                                                         ██║     ╚██████╔╝██║  ██╗███████╗██║  ██║
                                                         ╚═╝      ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
                                                                                                 \s
            """;

    public static final String TITLE_BLACKJACK = """
                                    ██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗\s
                                    ██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗
                                    ██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║
                                    ██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║
                                    ╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝
                                     ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝\s
                                     
                                         ██████╗ ██╗      █████╗  ██████╗██╗  ██╗     ██╗ █████╗  ██████╗██╗  ██╗
                                         ██╔══██╗██║     ██╔══██╗██╔════╝██║ ██╔╝     ██║██╔══██╗██╔════╝██║ ██╔╝
                                         ██████╔╝██║     ███████║██║     █████╔╝      ██║███████║██║     █████╔╝\s
                                         ██╔══██╗██║     ██╔══██║██║     ██╔═██╗ ██   ██║██╔══██║██║     ██╔═██╗\s
                                         ██████╔╝███████╗██║  ██║╚██████╗██║  ██╗╚█████╔╝██║  ██║╚██████╗██║  ██╗
                                         ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
                                                                                                                \s
            """;

    public static final String CASINO_MINDSWAP_ACADEMY = """
                     ██████╗ █████╗ ███████╗██╗███╗   ██╗ ██████╗     ███╗   ███╗██╗███╗   ██╗██████╗ ███████╗██╗    ██╗ █████╗ ██████╗      █████╗  ██████╗ █████╗ ██████╗ ███████╗███╗   ███╗██╗   ██╗
                    ██╔════╝██╔══██╗██╔════╝██║████╗  ██║██╔═══██╗    ████╗ ████║██║████╗  ██║██╔══██╗██╔════╝██║    ██║██╔══██╗██╔══██╗    ██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝████╗ ████║╚██╗ ██╔╝
                    ██║     ███████║███████╗██║██╔██╗ ██║██║   ██║    ██╔████╔██║██║██╔██╗ ██║██║  ██║███████╗██║ █╗ ██║███████║██████╔╝    ███████║██║     ███████║██║  ██║█████╗  ██╔████╔██║ ╚████╔╝\s
                    ██║     ██╔══██║╚════██║██║██║╚██╗██║██║   ██║    ██║╚██╔╝██║██║██║╚██╗██║██║  ██║╚════██║██║███╗██║██╔══██║██╔═══╝     ██╔══██║██║     ██╔══██║██║  ██║██╔══╝  ██║╚██╔╝██║  ╚██╔╝ \s
                    ╚██████╗██║  ██║███████║██║██║ ╚████║╚██████╔╝    ██║ ╚═╝ ██║██║██║ ╚████║██████╔╝███████║╚███╔███╔╝██║  ██║██║         ██║  ██║╚██████╗██║  ██║██████╔╝███████╗██║ ╚═╝ ██║   ██║  \s
                     ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═════╝ ╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝         ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═════╝ ╚══════╝╚═╝     ╚═╝   ╚═╝  \s
                                                                                                                                                                                                       \s
            """;

    private Messages() {
    }


}
