package academy.mindswap.p1g2.casino.server.games.slotMachine.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;

import java.io.IOException;

public class DoubleBetCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        if (spot instanceof Slot) {
            ((Slot) spot).doubleBet(clientHandler);
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
