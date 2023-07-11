package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.Player;
import academy.mindswap.p1g2.casino.server.games.poker.rule.Evaluator;
import academy.mindswap.p1g2.casino.server.games.poker.rule.EvaluatorFactory;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;

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
        Evaluator evaluator = EvaluatorFactory.make();
        for (Player player : table.getPlayers()) {
            player.addCards(table.getCards());
            player.setHandScore(evaluator.evaluateHand(player.getCards()));
        }

        table.getPlayers().stream()
                .sorted()
                .findFirst().
                ifPresent(winnerPlayer -> table.getPlayers().forEach(player -> {
                    try {
                        if (player == winnerPlayer) {
                            player.sendMessageToPlayer(String.format("You won the hand with %s and won %d poker chips.", player.getHandScore().getEvaluator().getName(), table.getPotValue()));
                            player.addBalance(table.getPotValue());
                        } else {
                            player.sendMessageToPlayer(String.format("%s won the hand with %s and won %d poker chips.",
                                    winnerPlayer.getClientHandler().getUsername(),
                                    player.getHandScore().getEvaluator().getName(),
                                    table.getPotValue()
                            ));
                        }
                        table.receiveCardsFromPlayer(player.returnCards());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
        table.resetHand();

    }
}
