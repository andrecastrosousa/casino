package academy.mindswap.p1g2.casino.server.games.poker.street;

public class ShowdownStreet extends StreetImpl {
    @Override
    public StreetType nextStreet() {
        return StreetType.PRE_FLOP;
    }
}
