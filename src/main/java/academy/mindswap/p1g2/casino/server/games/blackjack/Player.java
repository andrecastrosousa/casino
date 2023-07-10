package academy.mindswap.p1g2.casino.server.games.blackjack;

/**
 *  Player name = client username;
 */
public class Player {
        private final String name;
        public int cardValue;

        public Player(String name){
            this.name = name;
        }

        public String getPlayerName(){
            return name;
        }

        public int score(int cardValue){
            return this.cardValue += cardValue;
        }

        public int getScore(){
            return score(cardValue);
        }
    }
