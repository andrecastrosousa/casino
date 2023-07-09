package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public class HandScore {
    private final List<Card> cards;
    private int score;

    public HandScore(int score, List<Card> cards) {
        this.cards = cards;
        this.score = score;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public String toString() {
        return "HandScore{" +
                "cards=" + cards +
                ", score=" + score +
                '}';
    }
}
