package academy.mindswap.p1g2.casino.server.games.poker.command;

import academy.mindswap.p1g2.casino.server.command.*;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Arrays;

public enum PokerCommands {
    ALL_IN("/allIn", "Whisper to someone", new AllInCommand()),
    CALL("/call", "List all users", new CallCommand()),
    CHECK("/check", "List all commands", new CheckCommand()),
    FOLD("/fold", "Quit", new FoldCommand()),
    RAISE("/raise", "Change your name", new RaiseCommand()),
    UNKNOWN("", "Unknown command", new UnknownCommand());

    private final String command;

    private final String description;

    private final CommandHandler handler;

    PokerCommands(String command, String description, CommandHandler handler) {
        this.command = command;
        this.description = description;
        this.handler = handler;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static CommandHandler getCommandHandlerByString(String message) {
        return Arrays.stream(PokerCommands.values())
                .filter(commands -> commands.getCommand().equals(message))
                .map(command -> command.handler)
                .findFirst()
                .orElse(UNKNOWN.handler);
    }

    public static String listCommands() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.POKER_SEPARATOR);
        for(PokerCommands command : PokerCommands.values()) {
            if(command != UNKNOWN) {
                message.append(command.getCommand()).append(" -> ").append(command.getDescription()).append("\n");
            }
        }
        message.append(Messages.SEPARATOR);
        return message.toString();
    }
}
