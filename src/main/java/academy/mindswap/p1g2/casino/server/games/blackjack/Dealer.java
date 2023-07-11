package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private List<Card> cards;
    private ArrayList<Card> hand;
    private int score;
    private PlaySound shufflingSound;
    private PlaySound giveCardSound;

    public Dealer(List<Card> cards) {
        this.cards = cards;
        hand = new ArrayList<>(2);
        shufflingSound = new PlaySound("../casino/sounds/cards_shuffling_sound.wav");
        giveCardSound = new PlaySound("../casino/sounds/give_card_sound.wav");
    }

    public void shuffle() throws InterruptedException {
        playShufflingSound();
        Thread.sleep(8000);
        Collections.shuffle(cards);
    }

    private void playShufflingSound() {
        shufflingSound.play();
    }
    private void playGiveCardSound() {
        giveCardSound.play();
    }

    public void giveCards(List<Player> players) throws InterruptedException {
        playGiveCardSound();
        Thread.sleep(3000);
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
                    receiveCardsFromPlayer(player.returnCards(Messages.FIRST_HAND_BURST));
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
        return Messages.DEALER_HAND +
                hand.get(0).toString();
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        message.append(Messages.DEALER_HAND);
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





