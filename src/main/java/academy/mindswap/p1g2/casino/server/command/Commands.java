package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Arrays;

public enum Commands {
    WHISPER("/whisper", "Whisper to someone", new WhisperCommand()),
    LIST("/list", "List all users", new ListUsersCommand()),
    HELP("/help", "List all commands", new HelpCommand()),
    QUIT("/quit", "Quit", new QuitCommand()),
    CHANGE_NAME("/changeName", "Change your name", new ChangeNameCommand()),
    NAME("/name", "See your name", new NameCommand()),
    UNKNOWN("", "Unknown command", new UnknownCommand());

    private final String command;

    private final String description;

    private final CommandHandler handler;

    Commands(String command, String description, CommandHandler handler) {
        this.command = command;
        this.description = description;
        this.handler = handler;
    }

    public static CommandHandler getCommandHandlerByString(String message) {
        return Arrays.stream(Commands.values())
                .filter(commands -> commands.getCommand().equals(message))
                .map(command -> command.handler)
                .findFirst()
                .orElse(UNKNOWN.handler);
    }

    public static String listCommands() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.COMMAND_SEPARATOR);
        for(Commands command : Commands.values()) {
            if(command != UNKNOWN) {
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
