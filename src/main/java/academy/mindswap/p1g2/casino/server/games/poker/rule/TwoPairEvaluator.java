package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TwoPairEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        Map<Card.Value, List<Card>> valueCounts = new HashMap<>();

        for(Card card: cards) {
            List<Card> cardsAdded = valueCounts.getOrDefault(card.getValue(), new ArrayList<>());
            cardsAdded.add(card);
            valueCounts.put(card.getValue(), cardsAdded);
        }

        List<List<Card>> cardsPair = valueCounts.values().stream()
                .filter(cards1 -> cards1.size() == 2).toList();

        if (cardsPair.size() == 2) {
            List<Card> newHand = new ArrayList<>();
            newHand.addAll(cardsPair.get(0));
            newHand.addAll(cardsPair.get(1));

            Card higherCard = cards.stream()
                    .filter(card -> !newHand.contains(card))
                    .max(Comparator.comparingInt(Card::getCardValueOnPoker))
                    .orElse(null);

            newHand.add(higherCard);

            return new HandScore(3000, newHand);
        }

        return evaluateNext(cards);
    }
}
