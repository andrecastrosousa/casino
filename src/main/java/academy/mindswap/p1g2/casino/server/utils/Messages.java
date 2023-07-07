package academy.mindswap.p1g2.casino.server.utils;

public class Messages {

    public static final String SEPARATOR = "-----------------------------------";
    public static final String USER_SEPARATOR = "------------- USERS ---------------\n";
    public static final String COMMAND_SEPARATOR = "------------ COMMANDS --------------\n";
    public static final String SERVER_RUNNING = "Server is running";
    public static final String SERVER_OPENING = "Server is accepting Clients";
    public static final String ROOM_WAITING = "\nWaiting for players to start the game...\n";
    public static final String ROOM_STARTED = "Game will start soon...";
    public static final String CLIENT_MESSAGE = "client sent: %s";
    public static final String CLIENT_ARRIVED = "New client arrived";
    public static final String CLIENT_WELCOME = "Welcome to our chat!";
    public static final String CLIENT_NAME = "Your name is %s";
    public static final String INSERT_COMMAND = "Insert a command";

    /* CARDS */
    public static final String CARD_HEART = """
              ____
             |%s   |
             |(\\/)|
             | \\/ |
             |   %s|
             `----`
            """;

    public static final String CARD_DIAMOND = """
              ____
             |%s   |
             | /\\ |
             | \\/ |
             |   %s|
             `----`
            """;

    public static final String CARD_SPADE = """
              ____
             |%s   |
             | /\\ |
             |(__)|
             | /\\%s|
             `----`
            """;

    public static final String CARD_CLUB = """
              ____
             |%s  |
             |  & |
             | &|&|
             |  |%s|
             `----`
            """;

    private Messages() {}
}
