package academy.mindswap.p1g2.casino.server.games.blackjack.commands;

import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.UnknownCommand;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.util.Arrays;

public enum BlackjackMove {

    HIT("/hit", "Bet all your poker chip", new HitCommand()),
    STAND("/stand", "Match the same bet made by other player", new StandCommand()),
    SURRENDER("/surrender", "List all commands", new SurrenderCommand()),

    UNKNOWN("", "Unknown command", new UnknownCommand());

    private final String command;

    private final String description;

    private final CommandHandler handler;

    BlackjackMove(String command, String description, CommandHandler handler) {
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
        return Arrays.stream(BlackjackMove.values())
                .filter(commands -> commands.getCommand().equals(message))
                .map(command -> command.handler)
                .findFirst()
                .orElse(UNKNOWN.handler);
    }

    public static String listCommands() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.POKER_SEPARATOR);
        for (BlackjackMove command : BlackjackMove.values()) {
            if (command != UNKNOWN) {
                message.append(command.getCommand()).append(" -> ").append(command.getDescription()).append("\n");
            }
        }
        message.append(Messages.SEPARATOR);
        return message.toString();
    }
}

