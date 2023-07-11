package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

@FunctionalInterface
public interface CommandHandler {
    void execute(Spot spot, ClientHandler clientHandler) throws IOException, InterruptedException;
}
