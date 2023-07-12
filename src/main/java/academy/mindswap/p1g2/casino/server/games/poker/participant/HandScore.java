package academy.mindswap.p1g2.casino.server.games.poker.participant;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.rule.EvaluatorType;

import java.util.List;

public class HandScore implements Comparable {
    private final List<Card> cards;
    private final EvaluatorType evaluator;
    private int score;

    public HandScore(int score, List<Card> cards, EvaluatorType evaluator) {
        this.cards = cards;
        this.score = score;
        this.evaluator = evaluator;
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

    public EvaluatorType getEvaluator() {
        return evaluator;
    }

    @Override
    public int compareTo(Object o) {
        HandScore handScore = (HandScore) o;
        return handScore.getScore() - getScore();
    }
}
