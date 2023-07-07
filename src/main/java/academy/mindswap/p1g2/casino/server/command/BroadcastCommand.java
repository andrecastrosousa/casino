package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class BroadcastCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        spot.broadcast(String.format("%s: %s", clientHandler.getUsername(), clientHandler.getMessage()), clientHandler);
    }
}
