package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.*;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.blackjack.commands.BlackjackMove;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;
import academy.mindswap.p1g2.casino.server.games.slotMachine.SlotMachine;
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
        if(commandInvoker.getSpot() instanceof Poker) {
            CommandHandler commandHandler = BetOption.getCommandHandlerByString(command);
            if(!(commandHandler instanceof UnknownCommand)) {
                invoke(commandHandler, clientHandler);
                return;
            }
        }else
            if(commandInvoker.getSpot() instanceof Slot) {
            CommandHandler commandHandler = SpinOption.getCommandHandlerByString(command);
            if(!(commandHandler instanceof UnknownCommand)) {
                invoke(commandHandler, clientHandler);
                return;
            }
        } else if(commandInvoker.getSpot() instanceof Blackjack) {
                CommandHandler commandHandler = BlackjackMove.getCommandHandlerByString(command);
                if(!(commandHandler instanceof UnknownCommand)) {
                    invoke(commandHandler, clientHandler);
                    return;
                }
            }
        invoke(Commands.getCommandHandlerByString(command), clientHandler);
    }
}
