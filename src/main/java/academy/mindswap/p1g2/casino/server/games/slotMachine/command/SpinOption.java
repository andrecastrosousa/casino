package academy.mindswap.p1g2.casino.server.games.slotMachine.command;

import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.UnknownCommand;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Arrays;

public enum SpinOption {
    DOUBLE_BET("/double", "Double your bet!", new DoubleBetCommand()),
    SPIN("/spin", "Spin with 1 bet!", new SpinCommand()),

    UNKNOWN("", "Unknown command", new UnknownCommand());

    private final String command;

    private final String description;

    private final CommandHandler handler;

    SpinOption(String command, String description, CommandHandler handler) {
        this.command = command;
        this.description = description;
        this.handler = handler;
    }

    public static CommandHandler getCommandHandlerByString(String message) {
        return Arrays.stream(SpinOption.values())
                .filter(commands -> commands.getCommand().equals(message))
                .map(command -> command.handler)
                .findFirst()
                .orElse(UNKNOWN.handler);
    }

    public static String listCommands() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.POKER_SEPARATOR);
        for (SpinOption command : SpinOption.values()) {
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
