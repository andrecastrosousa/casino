package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.menu.MenuOption;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;

public class GameFactory {
    private GameFactory() {

    }

    public static Game getGameFromMenu(MenuOption menuOption) {
        return switch (menuOption) {
            case POKER -> new Poker();
            case BLACKJACK -> new Blackjack();
            case SLOT_MACHINE -> new Slot();
            case UNKNOWN -> null;
        };
    }
}
