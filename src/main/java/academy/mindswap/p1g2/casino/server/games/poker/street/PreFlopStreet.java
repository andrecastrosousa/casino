package academy.mindswap.p1g2.casino.server.games.poker.street;

public class PreFlopStreet extends StreetImpl {
    @Override
    public StreetType nextStreet() {
        return StreetType.FLOP;
    }
}
