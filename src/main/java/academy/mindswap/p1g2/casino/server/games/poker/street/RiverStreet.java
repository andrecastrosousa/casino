package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;

import java.io.IOException;

public class RiverStreet extends StreetImpl {
    public RiverStreet(PokerTable pokerTable) {
        super(pokerTable);
    }

    @Override
    public void nextStreet() throws InterruptedException {
        if(pokerTable.getPlayTimes() >= pokerTable.getPlayersPlaying().size() && canGoToNextStreet()) {
            pokerTable.setStreetType(StreetType.SHOWDOWN);
            pokerTable.initStreet();
        }
    }

    @Override
    public void execute() throws InterruptedException {
        pokerTable.burnCard();
        pokerTable.turnUpCard();

        pokerTable.getPlayers().forEach(player -> {
            try {
                player.sendMessage(pokerTable.showTableCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
