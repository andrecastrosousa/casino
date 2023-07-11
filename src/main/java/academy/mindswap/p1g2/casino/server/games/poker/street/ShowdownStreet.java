package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.Player;
import academy.mindswap.p1g2.casino.server.games.poker.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.rule.Evaluator;
import academy.mindswap.p1g2.casino.server.games.poker.rule.EvaluatorFactory;
import academy.mindswap.p1g2.casino.server.games.poker.table.Table;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.List;

public class ShowdownStreet extends StreetImpl {
    public ShowdownStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.PRE_FLOP);
        List<Player> playersToRemove = table.getPlayers().stream()
                .filter(player -> player.getCurrentBalance() == 0)
                .toList();
        playersToRemove.forEach(player -> {
            try {
                player.sendMessage(Messages.YOU_LOSE_ALL_CHIPS);
                table.removePlayer(player, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void execute() {
        Evaluator evaluator = EvaluatorFactory.make();
        for (Player player : table.getPlayers()) {
            PokerPlayer pokerPlayer = (PokerPlayer) player;
            pokerPlayer.addCards(table.getCards());
            pokerPlayer.setHandScore(evaluator.evaluateHand(pokerPlayer.getCards()));
        }

        table.getPlayers().stream()
                .sorted()
                .findFirst().
                ifPresent(winnerPlayer -> table.getPlayers().forEach(player -> {
                    PokerPlayer pokerPlayer = (PokerPlayer) player;

                    try {
                        if (player == winnerPlayer) {
                            player.sendMessage(String.format(Messages.YOU_WON_HAND, pokerPlayer.getHandScore().getEvaluator().getName(), table.getPotValue()));
                            player.addBalance(table.getPotValue());
                        } else {
                            player.sendMessage(String.format(Messages.SOMEONE_WON_HAND_W,
                                    winnerPlayer.getClientHandler().getUsername(),
                                    pokerPlayer.getHandScore().getEvaluator().getName(),
                                    table.getPotValue()
                            ));
                        }
                        table.receiveCardsFromPlayer(pokerPlayer.returnCards());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
        table.resetHand();

    }
}
