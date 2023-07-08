package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Table;

public class RiverStreet extends StreetImpl {
    public RiverStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.SHOWDOWN);
    }

    @Override
    public void execute() {
        table.burnCard();
        for(int i = 0; i < 3; i++) {
            table.turnUpCard();
        }
    }
}
