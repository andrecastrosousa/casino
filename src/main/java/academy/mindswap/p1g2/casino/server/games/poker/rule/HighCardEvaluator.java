package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighCardEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = cards.stream()
                .sorted(Comparator.comparingInt(Card::getCardValueOnPoker))
                .skip(2)
                .toList();

        return new HandScore(newHand.get(newHand.size() - 1).getCardValueOnPoker(), newHand);

    }
}
