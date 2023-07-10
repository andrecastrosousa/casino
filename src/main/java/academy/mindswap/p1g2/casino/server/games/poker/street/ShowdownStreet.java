package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.poker.HandScore;
import academy.mindswap.p1g2.casino.server.games.poker.rule.Evaluator;
import academy.mindswap.p1g2.casino.server.games.poker.rule.HandEvaluator;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.util.ArrayList;
import java.util.List;

public class ShowdownStreet extends StreetImpl {
    public ShowdownStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.PRE_FLOP);
    }

    @Override
    public void execute() {
        // TODO: EVALUATE ALL HANDS
        List<Card> cardList = new ArrayList<>(List.of(
                new Card(Card.Value.FIVE, Card.Suit.SPADES),
                new Card(Card.Value.TWO, Card.Suit.CLUBS),
                new Card(Card.Value.TEN, Card.Suit.DIAMONDS),
                new Card(Card.Value.SIX, Card.Suit.CLUBS),
                new Card(Card.Value.JACK, Card.Suit.CLUBS),
                new Card(Card.Value.ACE, Card.Suit.DIAMONDS),
                new Card(Card.Value.QUEEN, Card.Suit.SPADES)
        ));

        Evaluator evaluator = HandEvaluator.getEvaluatorChain();
        HandScore returnValue = evaluator.evaluateHand(cardList);
        System.out.println(returnValue);

        /*cards.addAll(tableCards);
        cards.addAll(turnDownCards);
        shuffle();*/
    }
}
