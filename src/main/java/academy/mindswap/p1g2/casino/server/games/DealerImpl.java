package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.Player;
import academy.mindswap.p1g2.casino.server.games.deck.Card;

import java.util.Collections;
import java.util.List;

public abstract class DealerImpl implements Dealer {
    protected final List<Card> cards;
    private PlaySound shufflingSound;
    private PlaySound giveCardSound;

    public DealerImpl(List<Card> cards) {
        this.cards = cards;
    }

    private void playShufflingSound() {
        shufflingSound.play();
    }
    private void playGiveCardSound() {
        giveCardSound.play();
    }

    @Override
    public void shuffle(){
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
        for (Card card: cards) {
            this.cards.add(0, card);
        }
    }
}
