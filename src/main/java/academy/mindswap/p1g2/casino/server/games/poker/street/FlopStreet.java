package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.deck.BoardChecker;
import academy.mindswap.p1g2.casino.server.games.poker.table.PokerTable;

import java.io.IOException;

public class FlopStreet extends StreetImpl {
    public FlopStreet(PokerTable pokerTable) {
        super(pokerTable);
    }

    @Override
    public void nextStreet() throws InterruptedException {
        if (pokerTable.getPlayTimes() >= pokerTable.getPlayersPlaying().size() && canGoToNextStreet()) {
            pokerTable.setStreetType(StreetType.TURN);
            pokerTable.initStreet();
        }
    }

    @Override
    public void execute() throws InterruptedException {
        BoardChecker boardChecker = BoardChecker.getInstance();
        pokerTable.burnCard();
        for (int i = 0; i < 3; i++) {
            pokerTable.turnUpCard();
        }

        pokerTable.getPlayers().forEach(player -> {
            try {
                player.sendMessage(boardChecker.getBoard(pokerTable.getCards()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
