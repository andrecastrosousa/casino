package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.games.deck.Card;
import academy.mindswap.p1g2.casino.server.games.DealerImpl;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.games.deck.DeckGenerator;
import academy.mindswap.p1g2.casino.server.Player;

import java.io.IOException;
import java.util.*;
public class PokerDealer extends DealerImpl {

    private PlaySound shufflingSound;
    private PlaySound giveCardSound;

    public PokerDealer() {
        super(DeckGenerator.getDeckOfCards());
        shufflingSound = new PlaySound("../casino/sounds/cards_shuffling_sound.wav");
        giveCardSound = new PlaySound("../casino/sounds/give_card_sound.wav");
    }

    public void pickTableCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    private void playShufflingSound() {
        shufflingSound.play();
    }
    private void playGiveCardSound() {
        giveCardSound.play();
    }

    @Override
    public void distributeCards(List<Player> players) {
        for(int i = 0; i < 2; i++) {
            for(Player player : players) {
                ((PokerPlayer) player).receiveCard(cards.remove(cards.size() - 1));
            }
        }

        players.forEach(player -> {
            try {
                player.sendMessage(((PokerPlayer) player).showCards());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
