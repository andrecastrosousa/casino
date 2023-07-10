package academy.mindswap.p1g2.casino.server.games.poker.rule;

public enum EvaluatorType {
    ROYAL_FLUSH(10000, "Royal flush"),
    STRAIGHT_FLUSH(9000, "Straight flush"),
    FOUR_OF_KIND(8000, "Four of a kind"),
    FULL_HOUSE(7000, "Full house"),
    FLUSH(6000, "Flush"),
    STRAIGHT(5000, "Straight"),
    THREE_OF_KIND(4000, "Three of a kind"),
    TWO_PAIR(3000, "Two pairs"),
    ONE_PAIR(2000, "One pair"),
    HIGH_CARD(1000, "High card");

    private final int score;
    private final String name;

    EvaluatorType(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
