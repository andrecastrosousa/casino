package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.util.Collections;
import java.util.List;

public abstract class DealerImpl implements Dealer {
    protected final List<Card> cards;
    private final PlaySound shufflingSound;
    private final PlaySound giveCardSound;

    public DealerImpl(List<Card> cards) {
        this.cards = cards;
        shufflingSound = new PlaySound("../casino/sounds/cards_shuffling_sound.wav");
        giveCardSound = new PlaySound("../casino/sounds/give_card_sound.wav");
    }

    protected void playShufflingSound() {
        shufflingSound.play();
    }

    protected void playGiveCardSound() {
        giveCardSound.play();
    }

    @Override
    public void shuffle() {
        playShufflingSound();
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Collections.shuffle(cards);
    }

    @Override
    public Card giveCard() {
        playGiveCardSound();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cards.remove(cards.size() - 1);
    }

    @Override
    public abstract void distributeCards(List<Player> players);

    @Override
    public void receiveCardsFromPlayer(List<Card> cards) {
        for (Card card : cards) {
            this.cards.add(0, card);
        }
    }
}
