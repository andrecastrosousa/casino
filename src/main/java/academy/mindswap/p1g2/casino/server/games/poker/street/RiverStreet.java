package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;

public class RiverStreet extends StreetImpl {
    public RiverStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() throws InterruptedException {
        if(table.getPlayTimes() >= table.getPlayersPlaying().size() && canGoToNextStreet()) {
            table.setStreetType(StreetType.SHOWDOWN);
            table.initStreet();
        }
    }

    @Override
    public void execute() throws InterruptedException {
        table.burnCard();
        table.turnUpCard();

        table.getPlayers().forEach(player -> {
            try {
                player.sendMessage(table.showTableCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
