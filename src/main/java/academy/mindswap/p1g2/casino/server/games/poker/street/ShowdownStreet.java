package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

public class ShowdownStreet extends StreetImpl {
    public ShowdownStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.PRE_FLOP);
    }

    @Override
    public void execute() {
        // TODO: EVALUATE ALL HANDS
        /*cards.addAll(tableCards);
        cards.addAll(turnDownCards);
        shuffle();*/
    }
}
