package academy.mindswap.p1g2.casino.server.games.poker.button;

public class Blind {
    private final int smallBind;
    private final int bigBind;

    public Blind() {
        smallBind = 20;
        bigBind = 40;
    }

    public int getBigBind() {
        return bigBind;
    }

    public int getSmallBind() {
        return smallBind;
    }
}
