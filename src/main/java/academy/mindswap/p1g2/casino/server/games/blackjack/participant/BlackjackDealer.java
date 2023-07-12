package academy.mindswap.p1g2.casino.server.games.blackjack.participant;

import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.deck.BoardChecker;
import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.DealerImpl;
import academy.mindswap.p1g2.casino.server.games.deck.DeckGenerator;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackjackDealer extends DealerImpl {
    private final ArrayList<Card> hand;
    private int score;

    public BlackjackDealer() {
        super(DeckGenerator.getDeckOfCards());
        hand = new ArrayList<>(2);
    }

    @Override
    public void distributeCards(List<Player> players) {
        BoardChecker boardChecker = BoardChecker.getInstance();
        handScore(cards.remove(cards.size() - 1));
        handScore(cards.remove(cards.size() - 1));

        for (Player player : players) {
            ((BlackjackPlayer) player).receiveCard(cards.remove(cards.size() - 1));
            ((BlackjackPlayer) player).receiveCard(cards.remove(cards.size() - 1));
        }
        players.forEach(player -> {
            try {
                player.sendMessage(boardChecker.getBoard(((BlackjackPlayer)player).getHand()));
                player.sendMessage(boardChecker.getBoard(showFirstCard()));
                if(((BlackjackPlayer) player).getScore() > Blackjack.HIGH_SCORE){
                    receiveCardsFromPlayer(((BlackjackPlayer) player).returnCards(Messages.FIRST_HAND_BURST));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<Card> showFirstCard() {
        return List.of(hand.get(0));
    }

    public String showCards() {
        BoardChecker boardChecker = BoardChecker.getInstance();
        return boardChecker.getBoard(hand);
    }

    public void handScore(Card card){
        hand.add(card);
        score += card.getCardValueOnBlackjack();
    }

    public int getScore() {
        return score;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void resetHand() {
        cards.addAll(hand);
        hand.clear();
        score = 0;
    }
}





