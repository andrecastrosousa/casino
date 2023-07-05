package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

public class ConnectCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) {
        clientHandler.changeSpot(spot);
    }
}
