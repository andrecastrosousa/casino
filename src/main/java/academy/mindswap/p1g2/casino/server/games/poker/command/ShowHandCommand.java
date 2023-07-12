package academy.mindswap.p1g2.casino.server.games.poker.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;

import java.io.IOException;

public class ShowHandCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        if (spot instanceof Poker) {
            ((Poker) spot).showHand(clientHandler);
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
