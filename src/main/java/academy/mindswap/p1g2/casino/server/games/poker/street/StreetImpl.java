package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Player;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.util.Comparator;

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

    protected boolean canGoToNextStreet() {
        int maxBet = table.getHigherBet();

        long count = table.getPlayersPlaying().stream()
                .filter(player -> player.getBet() == maxBet)
                .count();

        return count == table.getPlayersPlaying().size();
    }
}
