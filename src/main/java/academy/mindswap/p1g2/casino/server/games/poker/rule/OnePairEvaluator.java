package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class OnePairEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        Map<Card.Value, List<Card>> valueCounts = new HashMap<>();

        for(Card card: cards) {
            List<Card> cardsAdded = valueCounts.getOrDefault(card.getValue(), new ArrayList<>());
            cardsAdded.add(card);
            valueCounts.put(card.getValue(), cardsAdded);
        }

        List<Card> newHand = valueCounts.values().stream()
                .filter(cards1 -> cards1.size() == 2)
                .findFirst()
                .orElse(new ArrayList<>());

        if(newHand.size() == 2) {
            List<Card> higherCard = cards.stream()
                    .filter(card -> !newHand.contains(card))
                    .sorted(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)))
                    .toList();
            newHand.addAll(higherCard.subList(0, 3));
            return new HandScore(2000, newHand);
        }

        return evaluateNext(cards);
    }
}
