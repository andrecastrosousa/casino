package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeOfAKindEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {

        Map<Card.Value, Integer> valuesCount = new HashMap<>();

        for (Card card: cards) {
            int count = valuesCount.getOrDefault(card.getValue(), 0);
            valuesCount.put(card.getValue(), count + 1);
        }

        if (valuesCount.containsValue(3)) {
            return 5000;
        }

        return 0;
    }
}
