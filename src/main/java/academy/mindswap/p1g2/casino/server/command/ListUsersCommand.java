package academy.mindswap.p1g2.casino.server.command;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Room;
import academy.mindswap.p1g2.casino.server.Spot;

import java.io.IOException;

public class ListUsersCommand implements CommandHandler {
    @Override
    public void execute(Spot room, ClientHandler clientHandler) throws IOException {
        if(room instanceof Room) {
            ((Room) room).listUsers(clientHandler);
            return;
        }
        room.whisper(Commands.UNKNOWN.getDescription(), clientHandler.getUsername());
    }
}
