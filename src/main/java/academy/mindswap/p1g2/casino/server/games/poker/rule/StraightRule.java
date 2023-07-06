package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.List;

public class StraightRule implements Rule {
    @Override
    public boolean evaluateHand() {
        return false;
    }

    @Override
    public boolean evaluateNext(List<Card> cards) {
        return false;
    }

    @Override
    public void setNextRule(Rule rule) {

    }
}
