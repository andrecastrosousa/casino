package academy.mindswap.p1g2.casino.server.games.poker.street;

public abstract class StreetImpl implements Street {

    public static Street buildStreet(StreetType streetType) {
        return switch (streetType) {
            case PRE_FLOP -> new PreFlopStreet();
            case FLOP -> new FlopStreet();
            case TURN -> new TurnStreet();
            case RIVER -> new RiverStreet();
            case SHOWDOWN -> new ShowdownStreet();
        };
    }

    @Override
    public abstract StreetType nextStreet();
}
