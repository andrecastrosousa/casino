package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.DealerImpl;
import academy.mindswap.p1g2.casino.server.games.deck.DeckGenerator;
import academy.mindswap.p1g2.casino.server.games.Player;

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
        handScore(cards.remove(cards.size() - 1));
        handScore(cards.remove(cards.size() - 1));

        for (Player player : players) {
            ((BlackjackPlayer) player).receiveCard(cards.remove(cards.size() - 1));
            ((BlackjackPlayer) player).receiveCard(cards.remove(cards.size() - 1));
        }
        players.forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(((BlackjackPlayer) player).showCards());
                player.getClientHandler().sendMessageUser(showFirstCard());
                if(((BlackjackPlayer) player).getScore() > Blackjack.HIGH_SCORE){
                    receiveCardsFromPlayer(((BlackjackPlayer) player).returnCards("Your first hand burst!!!"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String showFirstCard() {
        return "Dealer hand: \n" +
                hand.get(0).toString();
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        message.append("Dealer hand: ");
        hand.forEach(card -> message.append(card.toString()));
        return message.toString();
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





