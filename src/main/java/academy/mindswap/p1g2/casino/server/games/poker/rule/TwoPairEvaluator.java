package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TwoPairEvaluator extends HandEvaluator {
    @Override
    public int evaluateHand(List<Card> cards) {
        Map<Card.Value, Integer> valueCounts = new HashMap<>();

        for(Card card: cards) {
            int count = valueCounts.getOrDefault(card.getValue(), 0);
            valueCounts.put(card.getValue(), count + 1);
        }

        long pairsQuantity = valueCounts.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .count();

        if (pairsQuantity == 2) {
            return 4000;
        }

        return 0;
    }
}
