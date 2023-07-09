package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class FullHouseEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        Map<Card.Value, List<Card>> valueCounts = new HashMap<>();

        for(Card card: cards) {
            List<Card> cardsAdded = valueCounts.getOrDefault(card.getValue(), new ArrayList<>());
            cardsAdded.add(card);
            valueCounts.put(card.getValue(), cardsAdded);
        }

        List<List<Card>> cardsPair = valueCounts.values().stream()
                .filter(cards1 -> cards1.size() == 2 || cards1.size() == 3).toList();

        if (cardsPair.size() >= 2 && (cardsPair.get(0).size() == 3 || cardsPair.get(1).size() == 3)) {
            List<Card> newHand = new ArrayList<>();
            newHand.addAll(cardsPair.get(0));
            newHand.addAll(cardsPair.get(1));

            return new HandScore(7000, newHand);
        }

        return evaluateNext(cards);
    }
}
