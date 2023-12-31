package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.*;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.blackjack.commands.BlackjackMove;
import academy.mindswap.p1g2.casino.server.games.menu.MenuOption;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;
import academy.mindswap.p1g2.casino.server.games.slotMachine.command.SpinOption;

import java.io.IOException;

public class MessageSender {
    private final CommandInvoker commandInvoker;

    public MessageSender(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public void changeSpot(Spot spot) {
        commandInvoker.changeSpot(spot);
    }

    public void dealWithCommands(String message, ClientHandler clientHandler) throws IOException, InterruptedException {
        if (message.startsWith("/")) {
            parseCommand(clientHandler);
        } else {
            invoke(new BroadcastCommand(), clientHandler);
        }
    }

    private void invoke(CommandHandler commandHandler, ClientHandler clientHandler) throws IOException, InterruptedException {
        commandInvoker.setMessageCommand(commandHandler);
        commandInvoker.invoke(clientHandler);
    }

    private void parseCommand(ClientHandler clientHandler) throws IOException, InterruptedException {
        String command = clientHandler.getMessage().split(" ")[0];
        CommandHandler commandHandler = null;

        if (commandInvoker.getSpot() instanceof Poker) {
            commandHandler = BetOption.getCommandHandlerByString(command);
        } else if (commandInvoker.getSpot() instanceof Slot) {
            commandHandler = SpinOption.getCommandHandlerByString(command);
        } else if (commandInvoker.getSpot() instanceof Blackjack) {
            commandHandler = BlackjackMove.getCommandHandlerByString(command);
        } else if (commandInvoker.getSpot() instanceof WaitingRoom) {
            commandHandler = MenuOption.getCommandHandlerByString(command);
        }

        if (commandHandler != null && !(commandHandler instanceof UnknownCommand)) {
            invoke(commandHandler, clientHandler);
            return;
        }

        invoke(Commands.getCommandHandlerByString(command), clientHandler);
    }
}
