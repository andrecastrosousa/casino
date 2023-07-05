package academy.mindswap.p1g2.casino.server.games;

public class Player {
        private String name;
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
