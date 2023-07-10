package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

public class PreFlopStreet extends StreetImpl {
    public PreFlopStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        if(table.getPlayTimes() >= table.getPlayers().size() && canGoToNextStreet()) {
            table.setStreetType(StreetType.FLOP);
            table.initStreet();
        }
    }

    @Override
    public void execute() {
        table.startHand();
    }
}
