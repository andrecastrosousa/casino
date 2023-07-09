package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public interface Evaluator {
    int evaluateHand(List<Card> cards);
    int evaluateNext(List<Card> cards);
    void setNextEvaluator(Evaluator evaluator);

}
