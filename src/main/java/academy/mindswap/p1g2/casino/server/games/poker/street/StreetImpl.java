package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

public abstract class StreetImpl implements Street, StreetHandler {

    protected Table table;

    public StreetImpl(Table table) {
        this.table = table;
    }

    public static StreetImpl buildStreet(Table table) {
        return switch (table.getStreetType()) {
            case PRE_FLOP -> new PreFlopStreet(table);
            case FLOP -> new FlopStreet(table);
            case TURN -> new TurnStreet(table);
            case RIVER -> new RiverStreet(table);
            case SHOWDOWN -> new ShowdownStreet(table);
        };
    }

    @Override
    public abstract void nextStreet();

    @Override
    public abstract void execute();
}
