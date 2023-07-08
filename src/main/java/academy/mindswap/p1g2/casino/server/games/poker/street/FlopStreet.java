package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

public class FlopStreet extends StreetImpl {
    public FlopStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.TURN);
    }

    @Override
    public void execute() {
        table.burnCard();
        for(int i = 0; i < 3; i++) {
            table.turnUpCard();
        }
    }
}
