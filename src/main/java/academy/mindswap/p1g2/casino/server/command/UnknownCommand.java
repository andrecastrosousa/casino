package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class UnknownCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
