package academy.mindswap.p1g2.casino.server.games.poker.button;

public class TheButton implements Button {

    @Override
    public ButtonType getType() {
        return ButtonType.THE_BUTTON;
    }

    @Override
    public int getBet() {
        return 0;
    }
}
