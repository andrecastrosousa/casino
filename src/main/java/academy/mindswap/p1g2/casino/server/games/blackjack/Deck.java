package academy.mindswap.p1g2.casino.server.games.blackjack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    public List<Integer> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    /**
     * This method generate a deck of cards to play;
     */
    public List generateDeck() {
        Random rd = new Random();
        int cardValue = rd.nextInt(13 - 1);
        for (int i = 0; i < cards.size(); i++) {
            cards.add(i, cardValue);
        }
        return cards;
    }

    public int drawCard() {
        if(cards.isEmpty()){
            return 0;
        }
        return cards.remove(cards.size() - 1);
    }

    private class Card{
        private final int value;

        public Card(int value){
            this.value = value;
        }

        private int getValue(){
            return value;
        }

    }

}
