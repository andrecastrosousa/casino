package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class HandEvaluator implements Evaluator {
    private Evaluator nextEvaluator;

    @Override
    public abstract HandScore evaluateHand(List<Card> cards);

    @Override
    public HandScore evaluateNext(List<Card> cards) {
        if(nextEvaluator != null) {
            return nextEvaluator.evaluateHand(cards);
        }
        return null;
    }

    public static Evaluator getEvaluatorChain() {
        Evaluator evaluatorChain = new RoyalFlushEvaluator();
        Evaluator straightFlushEvaluator = new StraightFlushEvaluator();
        evaluatorChain.setNextEvaluator(straightFlushEvaluator);
        Evaluator fourOfAKindEvaluator = new FourOfAKindEvaluator();
        straightFlushEvaluator.setNextEvaluator(fourOfAKindEvaluator);
        Evaluator fullHouseEvaluator = new FullHouseEvaluator();
        fourOfAKindEvaluator.setNextEvaluator(fullHouseEvaluator);

        Evaluator flushEvaluator = new FlushEvaluator();
        fullHouseEvaluator.setNextEvaluator(flushEvaluator);

        Evaluator straightEvaluator = new StraightEvaluator();
        flushEvaluator.setNextEvaluator(straightEvaluator);
        Evaluator threeOfKindEvaluator = new ThreeOfAKindEvaluator();
        straightEvaluator.setNextEvaluator(threeOfKindEvaluator);
        Evaluator twoPairEvaluator = new TwoPairEvaluator();
        threeOfKindEvaluator.setNextEvaluator(twoPairEvaluator);
        Evaluator onePairEvaluator = new OnePairEvaluator();
        twoPairEvaluator.setNextEvaluator(onePairEvaluator);
        Evaluator highCardEvaluator = new HighCardEvaluator();
        onePairEvaluator.setNextEvaluator(highCardEvaluator);

        return evaluatorChain;
    }

    @Override
    public void setNextEvaluator(Evaluator evaluator) {
        this.nextEvaluator = evaluator;
    }

    protected boolean isFlush(List<Card> cards, AtomicReference<Card.Suit> suitFlush) {
        return Arrays.stream(Card.getSuits())
                .anyMatch(suit -> {
                    if(cards.stream()
                            .filter(card -> card.getSuit() == suit)
                            .count() >= 5) {
                        suitFlush.set(suit);
                        return true;
                    }
                    return false;
                });
    }

    protected int isStraight(List<Card> cards) {
        int countVerifiedCards = 0;
        int indexOfLastCombinationCard = 0;

        for (int j = 1; j < cards.size(); j++) {
            if (countVerifiedCards == 4) {
                break;
            }
            if (cards.get(j).getCardValueOnPoker() == cards.get(j - 1).getCardValueOnPoker() - 1) {
                countVerifiedCards++;
                indexOfLastCombinationCard = j;
            } else {
                countVerifiedCards = 0;
                indexOfLastCombinationCard = 0;
            }
        }
        if(countVerifiedCards <= 4) {
            indexOfLastCombinationCard = 0;
        }
        return indexOfLastCombinationCard;
    }

    private Map<Card.Value, List<Card>> getPairsByValue(List<Card> cards) {
        Map<Card.Value, List<Card>> valueCounts = new HashMap<>();

        for(Card card: cards) {
            List<Card> cardsAdded = valueCounts.getOrDefault(card.getValue(), new ArrayList<>());
            cardsAdded.add(card);
            valueCounts.put(card.getValue(), cardsAdded);
        }

        return valueCounts;
    }

    protected List<Card> getPairCards(List<Card> cards, int numberOfCards) {
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)));

        Map<Card.Value, List<Card>> pairs = getPairsByValue(cards);

        return pairs.values().stream()
                .filter(cards1 -> cards1.size() >= numberOfCards)
                .findFirst()
                .orElse(new ArrayList<>());
    }

    protected List<List<Card>> getMultiplePairCards(List<Card> cards) {
        cards.sort(Collections.reverseOrder(Comparator.comparingInt(Card::getCardValueOnPoker)));

        Map<Card.Value, List<Card>> pairs = getPairsByValue(cards);

        return pairs.values().stream()
                .filter(cards1 -> cards1.size() >= 2)
                .toList();
    }

    protected List<Card> getHigherCards(List<Card> cards, List<Card> handCards) {
        return cards.stream()
                .filter(card -> !handCards.contains(card))
                .toList();
    }
}
