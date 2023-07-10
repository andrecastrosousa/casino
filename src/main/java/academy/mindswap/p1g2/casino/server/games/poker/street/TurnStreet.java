package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Player;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;
import java.util.Comparator;

public class TurnStreet extends StreetImpl {
    public TurnStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        if(table.getPlayTimes() >= table.getPlayersPlaying().size() && canGoToNextStreet()) {
            table.setStreetType(StreetType.RIVER);
            table.initStreet();
        }
    }

    @Override
    public void execute() {
        table.burnCard();
        table.turnUpCard();

        table.getPlayers().forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(table.showTableCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
