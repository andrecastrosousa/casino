package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.Comparator;
import java.util.List;

public class HighCardEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(Card::getCardValueOnPoker))
                .map(Card::getCardValueOnPoker)
                .findFirst().orElse(0);
    }
}
