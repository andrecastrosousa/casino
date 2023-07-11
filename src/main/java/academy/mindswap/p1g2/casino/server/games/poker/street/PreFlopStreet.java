package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;

public class PreFlopStreet extends StreetImpl {
    public PreFlopStreet(PokerTable pokerTable) {
        super(pokerTable);
    }

    @Override
    public void nextStreet() throws InterruptedException {
        if(pokerTable.getPlayTimes() >= pokerTable.getPlayers().size() && canGoToNextStreet()) {
            pokerTable.setStreetType(StreetType.FLOP);
            pokerTable.initStreet();
        }
    }

    @Override
    public void execute() throws InterruptedException {
        pokerTable.startHand();
    }
}
