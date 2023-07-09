package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class ThreeOfAKindEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {

        Map<Card.Value, List<Card>> valueCounts = new HashMap<>();

        for(Card card: cards) {
            List<Card> cardsAdded = valueCounts.getOrDefault(card.getValue(), new ArrayList<>());
            cardsAdded.add(card);
            valueCounts.put(card.getValue(), cardsAdded);
        }

        List<Card> newHand = valueCounts.values().stream()
                .filter(cards1 -> cards1.size() == 3)
                .findFirst()
                .orElse(new ArrayList<>());

        if(newHand.size() == 3) {
            List<Card> higherCard = cards.stream()
                    .filter(card -> !newHand.contains(card))
                    .sorted(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)))
                    .toList();
            newHand.addAll(higherCard.subList(0, 2));
            return new HandScore(4000, newHand);
        }

        return evaluateNext(cards);
    }
}
