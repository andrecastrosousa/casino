package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;

public class NameCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        spot.whisper(String.format(Messages.CLIENT_NAME, clientHandler.getUsername()), clientHandler.getUsername());
    }
}
