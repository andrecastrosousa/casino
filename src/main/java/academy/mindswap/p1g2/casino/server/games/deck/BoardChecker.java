package academy.mindswap.p1g2.casino.server.games.deck;

import java.util.ArrayList;
import java.util.List;

public class BoardChecker {
    private static BoardChecker instance;
    private List<Board> boards;

    private BoardChecker() {
        boards = new ArrayList<>(List.of(new BoardOneCard(), new BoardTwoCards(), new BoardThreeCards(), new BoardFourCards(), new BoardFiveCards()));
    }

    public static BoardChecker getInstance() {
        if (instance == null) {
            instance = new BoardChecker();
        }
        return instance;
    }

    public String getBoard(List<Card> cards) {
        for (Board board : boards) {
            if (board.canHandle(cards.size())) {
                return board.handle(cards);
            }
        }
        return "";
    }
}