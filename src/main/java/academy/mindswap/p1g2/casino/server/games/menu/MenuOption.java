package academy.mindswap.p1g2.casino.server.games.menu;

import academy.mindswap.p1g2.casino.server.command.CommandHandler;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.util.Arrays;

public enum MenuOption {
    POKER("/poker", "Poker", new PokerCommand()),
    BLACKJACK("/blackjack", "Blackjack", new BlackjackCommand()),
    SLOT_MACHINE("/slots", "Slots", new SlotsCommand()),
    UNKNOWN;

    private String option;
    private String description;
    private CommandHandler game;

    MenuOption(String option, String description, CommandHandler game) {
        this.option = option;
        this.description = description;
        this.game = game;
    }

    MenuOption() {
    }

    public static CommandHandler getCommandHandlerByString(String selectedOption) {
        return Arrays.stream(MenuOption.values())
                .filter(menuOption -> menuOption.getOption().equals(selectedOption))
                .map(menuOption -> menuOption.game)
                .findFirst()
                .orElse(UNKNOWN.game);
    }

    public static String showMenu() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.POKER_SEPARATOR);
        for (MenuOption menuOption : MenuOption.values()) {
            if (menuOption != UNKNOWN) {
                message.append(menuOption.getOption()).append(" -> ").append(menuOption.getDescription()).append("\n");
            }
        }
        message.append(Messages.SEPARATOR);
        return message.toString();
    }

    public String getDescription() {
        return description;
    }

    public String getOption() {
        return option;
    }

    public CommandHandler getGame() {
        return game;
    }
}
