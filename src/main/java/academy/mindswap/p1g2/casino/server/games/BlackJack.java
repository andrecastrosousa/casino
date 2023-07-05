package academy.mindswap.p1g2.casino.server.games;

import java.util.Random;

public class BlackJack {
    private static int HIGH_SCORE = 21;
    private static boolean isGameOver;
    private int cardValue;

    public static void main(String[] args) {
        System.out.println("Welcome to BlackJack game!");
        Player dealer = new Player("John");
        System.out.println(dealer.getPlayerName() + " is this round dealer.");
        Player player = new Player("Mary");
        System.out.println(dealer.getPlayerName() + " is playing against " + player.getPlayerName() + "!");
        System.out.println("Let's start!");
        BlackJack bj = new BlackJack();
        bj.playGame(player, dealer);
    }

    public void playGame(Player player, Player dealer) {
        while (!isGameOver) {
            if (!doTurn(player)) {
                printWinner(dealer);
                return;
            }
            if (!doTurn(dealer)) {
                printWinner(player);
                return;
            }
            Player winner = theWinnerIs(player, dealer);
            if (winner != null) {
                printWinner(winner);
                return;
            }
        }
    }

    private void printWinner(Player player) {
        System.out.printf("%s won the game!!!", player.getPlayerName());
    }

    public boolean doTurn(Player player) {
        int cardValue = generateCard();
        player.score(cardValue);
        System.out.printf("Player: %s take %d card and now has %d points %n", player.getPlayerName(), cardValue, player.score(cardValue));

        if(isBurst(player)){
            System.out.printf("Player %s burst %n",player.getPlayerName());
            isGameOver = true;
        }
        return false;
    }

    //
    private static Player theWinnerIs(Player player, Player dealer){
        if(player.getScore() == 21 && dealer.getScore() == 21){
            return dealer;
        }
        return player.getScore() == 21 ? player : dealer.getScore() == 21 ? dealer : null;
    }

    public int generateCard(){
        Random rd = new Random();
        cardValue = rd.nextInt(13) + 1;
        switch(cardValue){
            case 1:
                System.out.println("Ace");
                break;
            case 11:
                System.out.println("Jack");
                break;
            case 12:
                System.out.println("Queen");
                break;
            case 13:
                System.out.println("King");
                break;
            case 2,3,4,5,6,7,8,9,10:
                System.out.println(cardValue);
        }
        return cardValue;
    }
    private static boolean isBurst(Player player) {
        return player.score(player.cardValue) > HIGH_SCORE;
    }
}

