package academy.mindswap.p1g2.casino.server.games.poker.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;

import java.io.IOException;

public class RaiseCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        if(spot instanceof Poker) {
            ((Poker) spot).raise(clientHandler, Integer.parseInt(clientHandler.getMessage().replace(PokerCommands.RAISE.getCommand(), "").trim()));
            return;
        }
        spot.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
