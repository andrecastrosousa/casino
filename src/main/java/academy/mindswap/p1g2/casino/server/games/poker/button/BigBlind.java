package academy.mindswap.p1g2.casino.server.games.poker.button;

public class BigBlind implements Button {

    @Override
    public ButtonType getType() {
        return ButtonType.BIG_BLIND;
    }

    @Override
    public int getBet() {
        return 40;
    }
}
