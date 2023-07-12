package academy.mindswap.p1g2.casino.server.games.blackjack.commands;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;

import java.io.IOException;

public class HitCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException, InterruptedException {
        if (spot instanceof Blackjack) {
            ((Blackjack) spot).hit(clientHandler);
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
