package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.*;

public class Dealer {
    private final List<Card> cards;
    private PlaySound shufflingSound;
    private PlaySound giveCardSound;
    public Dealer() {
        cards = DeckGenerator.getDeckOfCards();
        shufflingSound = new PlaySound("../casino/sounds/cards_shuffling_sound.wav");
        giveCardSound = new PlaySound("../casino/sounds/give_card_sound.wav");
    }
    private void playShufflingSound() {
        shufflingSound.play();
    }
    private void playGiveCardSound() {
        giveCardSound.play();
    }
    public void shuffle() throws InterruptedException {
        playShufflingSound();
        Thread.sleep(8000);
        Collections.shuffle(cards);
    }

    public Card giveCard() throws InterruptedException {
        playGiveCardSound();
        Thread.sleep(3000);
        return cards.remove(cards.size() - 1);
    }

    public void distributeCards(List<Player> players) {
        for(int i = 0; i < 2; i++) {
            for(Player player : players) {
                player.receiveCard(cards.remove(cards.size() - 1));
            }
        }

        players.forEach(player -> {
            try {
                player.getClientHandler().sendMessageUser(player.showCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void receiveCardsFromPlayer(List<Card> cards) {
        for (Card card: cards) {
            this.cards.add(0, card);
        }
    }

    public void pickTableCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
}
