package academy.mindswap.p1g2.casino.server.games.blackjack.commands;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;

import java.io.IOException;

public class SurrenderCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        if (spot instanceof Blackjack) {
            ((Blackjack) spot).surrender(clientHandler);
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
