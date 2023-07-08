package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Table;

public class TurnStreet extends StreetImpl {
    public TurnStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.RIVER);
    }

    @Override
    public void execute() {
        table.burnCard();
        table.turnUpCard();
    }
}
