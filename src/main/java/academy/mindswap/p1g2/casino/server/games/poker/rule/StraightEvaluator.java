package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;

public class StraightEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)));

        boolean isStraight = true;
        int countVerifiedCards = 0;
        int indexOfLastCombinationCard = 0;

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

        List<Card> cardsFiltered = new ArrayList<>(cards);
        if (newHand.size() == 2) {
            cardsFiltered.remove(newHand.get(0));
        }

        for (int j = 1; j < cardsFiltered.size(); j++) {
            if (countVerifiedCards == 4) {
                isStraight = true;
                break;
            }
            if (cardsFiltered.get(j).getCardValueOnPoker() == cardsFiltered.get(j - 1).getCardValueOnPoker() - 1) {
                countVerifiedCards++;
                indexOfLastCombinationCard = j;
            } else {
                isStraight = false;
                countVerifiedCards = 0;
            }
        }

        if (isStraight || countVerifiedCards == 4) {
            return new HandScore(5000, cardsFiltered.subList(indexOfLastCombinationCard - 4, indexOfLastCombinationCard + 1));
        }

        return evaluateNext(cards);
    }
}
