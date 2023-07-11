package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.List;

public interface Evaluator {
    HandScore evaluateHand(List<Card> cards);
    HandScore evaluateNext(List<Card> cards);
    void setNextEvaluator(Evaluator evaluator);

}
