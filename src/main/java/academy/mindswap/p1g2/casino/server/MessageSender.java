package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.*;

import java.io.IOException;

public class MessageSender {
    private final CommandInvoker commandInvoker;

    public MessageSender(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public void changeSpot(Spot spot) {
        commandInvoker.changeSpot(spot);
    }

    public void dealWithCommands(String message, ClientHandler clientHandler) throws IOException {
        if(message.startsWith("/")) {
            parseCommand(clientHandler);
        } else {
            invoke(new BroadcastCommand(), clientHandler);
        }
    }

    private void invoke(CommandHandler commandHandler, ClientHandler clientHandler) throws IOException {
        commandInvoker.setMessageCommand(commandHandler);
        commandInvoker.invoke(clientHandler);
    }

    private void parseCommand(ClientHandler clientHandler) throws IOException {
        String command = clientHandler.getMessage().split(" ")[0];
        invoke(Commands.getCommandHandlerByString(command), clientHandler);
    }
}
