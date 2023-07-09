package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.rule.Evaluator;
import academy.mindswap.p1g2.casino.server.games.poker.rule.HandEvaluator;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.util.ArrayList;

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
        Evaluator evaluator = HandEvaluator.getEvaluatorChain();
        evaluator.evaluateHand(new ArrayList<>());

        /*cards.addAll(tableCards);
        cards.addAll(turnDownCards);
        shuffle();*/
    }
}
