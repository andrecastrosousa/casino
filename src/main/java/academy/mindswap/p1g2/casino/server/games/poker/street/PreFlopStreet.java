package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Table;

public class PreFlopStreet extends StreetImpl {
    public PreFlopStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.FLOP);
    }

    @Override
    public void execute() {

    }
}
