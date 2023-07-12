package academy.mindswap.p1g2.casino.server.games.poker.command;

import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.UnknownCommand;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Arrays;

public enum BetOption {
    ALL_IN("/allIn", "Bet all your poker chip", new AllInCommand()),
    CALL("/call", "Match the same bet made by other player", new CallCommand()),
    CHECK("/check", "List all commands", new CheckCommand()),
    FOLD("/fold", "Fold hand", new FoldCommand()),
    RAISE("/raise", "Raise value of minimum bet", new RaiseCommand()),
    BET("/bet", "Bet a value", new BetCommand()),
    HAND("/hand", "Show cards on your hand", new ShowHandCommand()),
    TABLE("/table", "Show cards on table", new ShowTableCommand()),
    BALANCE("/balance", "Show current balance", new BalanceCommand()),
    UNKNOWN("", "Unknown command", new UnknownCommand());

    private final String command;

    private final String description;

    private final CommandHandler handler;

    BetOption(String command, String description, CommandHandler handler) {
        this.command = command;
        this.description = description;
        this.handler = handler;
    }

    public static CommandHandler getCommandHandlerByString(String message) {
        return Arrays.stream(BetOption.values())
                .filter(commands -> commands.getCommand().equals(message))
                .map(command -> command.handler)
                .findFirst()
                .orElse(UNKNOWN.handler);
    }

    public static String listCommands() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.POKER_SEPARATOR);
        for (BetOption command : BetOption.values()) {
            if (command != UNKNOWN) {
                message.append(command.getCommand()).append(" -> ").append(command.getDescription()).append("\n");
            }
        }
        message.append(Messages.SEPARATOR);
        return message.toString();
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
