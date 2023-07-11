package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class CommandInvoker {
    private CommandHandler commandHandler;
    private Spot spot;

    public CommandInvoker(Spot spot) {
        this.spot = spot;
    }

    public void setMessageCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void invoke(ClientHandler clientHandler) throws IOException, InterruptedException {
        commandHandler.execute(spot, clientHandler);
    }

    public void changeSpot(Spot spot) {
        this.spot = spot;
    }

    public Spot getSpot() {
        return spot;
    }
}
