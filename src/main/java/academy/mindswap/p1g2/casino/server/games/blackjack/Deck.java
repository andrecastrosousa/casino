package academy.mindswap.p1g2.casino.server.games.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    public List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    /**
     * This method generate a deck of cards to play;
     * Deck with 48 cards with value of 10 and 4 cards with value of 11 (aces);
     */
    public void initializeDeck(){
        int numCards10 = 48;
        int numCards11 = 4;
        int cardValue1 = 10;
        int cardValue2 = 11;
        for(int i = 0; i < numCards10; i++) {
            cards.add(new Card(cardValue1));
        }
        for(int i = 0; i < numCards11; i++){
            cards.add(new Card(cardValue2));
        }
    }

    /**
     * Shuffle the cards with the two values that are passed as parameters in th method(initializeDeck());
     */
    public void shuffle(){
        Random rd = new Random();
        for(int i = cards.size() - 1; i > 0; i--){ // iterate over cardsList from de last position (excluding) to the index 1
            int j = rd.nextInt(i + 1); // generate random number between the number in the list;
            Card temp = cards.get(i); // uses a temp variable to store the value and trade position with the original;
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card drawCard() {
        if(cards.isEmpty()){
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public static class Card{
        private final int value;

        public Card(int value){
            this.value = value;
        }

        private int getValue(){
            return value;
        }


    }

}