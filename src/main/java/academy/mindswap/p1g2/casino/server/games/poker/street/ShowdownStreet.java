package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.poker.PokerPlayer;
import academy.mindswap.p1g2.casino.server.games.poker.rule.Evaluator;
import academy.mindswap.p1g2.casino.server.games.poker.rule.PokerEvaluatorFactory;
import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.List;

public class ShowdownStreet extends StreetImpl {
    public ShowdownStreet(PokerTable pokerTable) {
        super(pokerTable);
    }

    @Override
    public void nextStreet() {
        pokerTable.setStreetType(StreetType.PRE_FLOP);
        List<Player> playersToRemove = pokerTable.getPlayers().stream()
                .filter(player -> player.getCurrentBalance() == 0)
                .toList();
        playersToRemove.forEach(player -> {
            try {
                player.sendMessage(Messages.YOU_LOSE_ALL_CHIPS);
                pokerTable.removePlayer(player, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void execute() {
        Evaluator evaluator = PokerEvaluatorFactory.make();
        for (Player player : pokerTable.getPlayers()) {
            PokerPlayer pokerPlayer = (PokerPlayer) player;
            pokerPlayer.addCards(pokerTable.getCards());
            pokerPlayer.setHandScore(evaluator.evaluateHand(pokerPlayer.getCards()));
        }

        pokerTable.getPlayers().stream()
                .sorted()
                .findFirst().
                ifPresent(winnerPlayer -> pokerTable.getPlayers().forEach(player -> {
                    PokerPlayer pokerPlayer = (PokerPlayer) player;

                    try {
                        if (player == winnerPlayer) {
                            player.sendMessage(String.format(Messages.YOU_WON_HAND, pokerPlayer.getHandScore().getEvaluator().getName(), pokerTable.getPotValue()));
                            player.addBalance(pokerTable.getPotValue());
                        } else {
                            player.sendMessage(String.format(Messages.SOMEONE_WON_HAND_W,
                                    winnerPlayer.getClientHandler().getUsername(),
                                    ((PokerPlayer) winnerPlayer).getHandScore().getEvaluator().getName(),
                                    pokerTable.getPotValue()
                            ));
                        }
                        pokerTable.receiveCardsFromPlayer(pokerPlayer.returnCards());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
        pokerTable.resetHand();

    }
}
