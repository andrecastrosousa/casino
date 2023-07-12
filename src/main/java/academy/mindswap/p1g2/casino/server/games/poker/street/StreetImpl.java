package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.participant.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;

public abstract class StreetImpl implements Street, StreetHandler {

    protected PokerTable pokerTable;

    public StreetImpl(PokerTable pokerTable) {
        this.pokerTable = pokerTable;
    }

    public static StreetImpl buildStreet(PokerTable pokerTable) {
        return switch (pokerTable.getStreetType()) {
            case PRE_FLOP -> new PreFlopStreet(pokerTable);
            case FLOP -> new FlopStreet(pokerTable);
            case TURN -> new TurnStreet(pokerTable);
            case RIVER -> new RiverStreet(pokerTable);
            case SHOWDOWN -> new ShowdownStreet(pokerTable);
        };
    }

    @Override
    public abstract void nextStreet() throws InterruptedException;

    @Override
    public abstract void execute() throws InterruptedException;

    protected boolean canGoToNextStreet() {
        int maxBet = pokerTable.getHigherBet();

        long count = pokerTable.getPlayersPlaying().stream()
                .filter(player -> ((PokerPlayer) player).getBet() == maxBet)
                .count();

        return count == pokerTable.getPlayersPlaying().size();
    }
}
