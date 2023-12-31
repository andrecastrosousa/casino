package academy.mindswap.p1g2.casino.server.games.poker.rule;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.poker.participant.HandScore;

import java.util.Comparator;
import java.util.List;

public class HighCardEvaluator extends HandEvaluator {
    @Override
    public HandScore evaluateHand(List<Card> cards) {
        List<Card> newHand = cards.stream()
                .sorted(Comparator.comparingInt(Card::getCardValueOnPoker))
                .skip(2)
                .toList();

        return new HandScore(
                EvaluatorType.HIGH_CARD.getScore() + newHand.get(newHand.size() - 1).getCardValueOnPoker(),
                newHand,
                EvaluatorType.HIGH_CARD
        );

    }
}
