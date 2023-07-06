package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class ChangeNameCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        String message = clientHandler.getMessage().replace(Commands.CHANGE_NAME.getCommand(), "").trim();
        clientHandler.changeUsername(message);
    }
}
