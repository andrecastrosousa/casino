package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class WhisperCommand implements CommandHandler {
    @Override
    public void execute(Spot spot, ClientHandler clientHandler) throws IOException {
        String message = clientHandler.getMessage()
                .replace(Commands.WHISPER.getCommand(), "")
                .trim();
        String userToSend = message.split(" ")[0];

        spot.whisper(message, userToSend);
    }
}
