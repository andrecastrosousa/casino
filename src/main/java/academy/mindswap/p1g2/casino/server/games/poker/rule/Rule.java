package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public interface Rule {
    boolean evaluateHand();
    boolean evaluateNext(List<Card> cards);
    void setNextRule(Rule rule);

}
