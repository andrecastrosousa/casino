package academy.mindswap.p1g2.casino.server.games.menu;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.WaitingRoom;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;

import java.io.IOException;

public class PokerCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        if (spot instanceof WaitingRoom) {
            ((WaitingRoom) spot).poker(clientHandler);
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
