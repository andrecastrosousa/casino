package academy.mindswap.p1g2.casino.server.games.poker.table;

public class Pot {
    private int amount;

    public Pot() {
        amount = 0;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void resetPot() {
        amount = 0;
    }
}
