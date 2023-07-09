package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullHouseEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        Map<Card.Value, Integer> valueCounts = new HashMap<>();
        for(Card card: cards) {
            int count = valueCounts.getOrDefault(card.getValue(), 0);
            valueCounts.put(card.getValue(), count + 1);
        }

        if(valueCounts.containsValue(3) && valueCounts.containsValue(2)) {
            return 7000;
        }

        return evaluateNext(cards);
    }
}
