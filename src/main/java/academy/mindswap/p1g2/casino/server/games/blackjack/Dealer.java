package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.games.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private List<Card> cards;
    private ArrayList<Card> hand;
    private int score;

    public Dealer(List<Card> cards) {
        this.cards = cards;
        hand = new ArrayList<>(2);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void giveCards(List<Player> players) {
        handScore(cards.remove(cards.size() - 1));
        handScore(cards.remove(cards.size() - 1));

        for (Player player : players) {
            player.receiveCard(cards.remove(cards.size() - 1));
            player.receiveCard(cards.remove(cards.size() - 1));
        }
        players.forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(player.showCards());
                player.getClientHandler().sendMessageUser(showFirstCard());
                if(player.getScore() > Blackjack.HIGH_SCORE){
                    receiveCardsFromPlayer(player.returnCards("Your first hand burst!!!"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Card giveCard(){
        return cards.remove(cards.size() - 1);
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

    public void receiveCardsFromPlayer(List<Card> cards) {
        for (Card card : cards) {
            this.cards.add(0, card);
        }
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





