package academy.mindswap.p1g2.casino.server.games.poker.street;

import academy.mindswap.p1g2.casino.server.games.poker.table.Table;

import java.io.IOException;

public class TurnStreet extends StreetImpl {
    public TurnStreet(Table table) {
        super(table);
    }

    @Override
    public void nextStreet() {
        table.setStreetType(StreetType.RIVER);
    }

    @Override
    public void execute() {
        table.burnCard();
        table.turnUpCard();

        table.getPlayers().forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(table.showTableCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
