package academy.mindswap.p1g2.casino.server.games.blackjack;

import java.util.List;
import java.util.Random;

/** Rules:
 * 52 cards;
 * cards from 2 to 10 has value of 10;
 * jack, queen and king also worth 10;
 * ace values 1 or 11;
 * each player bets against the dealer;
 * each player have two cards faced up to start the round;
 * dealer also has two cards to start the round, one faced up and the other faced down;
 * "hit" - player asks another card (possible specific command for the game?!)
 * "split" - player has two identical cards and ask to split and each card is one bet;
 * "stand" - player don't want more cards;
 * "double down" - increase the initial bet by 100% and take exactly one more card;
 * "surrender" - lose half the bet and end the hand immediately;
 */
public class BlackJack {
    private static int HIGH_SCORE = 21;
    private static boolean isGameOver;
    private int cardValue;
    private int credits;
    public Player player = new Player("username");
    public Player dealer = new Player("username2");
    public Deck deck = new Deck();

    public void play(){
        System.out.println(Messages.WELCOME);
        System.out.println(Messages.START);
        System.out.println(Messages.PLAYER);
        player.getPlayerName();
        System.out.println(Messages.DEALER);
        dealer.getPlayerName();

    }

    private void hit(){
        deck.generateDeck();
        for(int cardValue : deck.cards){
            System.out.println(cardValue);
        }
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
        player.score(cardValue);
        System.out.printf("Player: %s take %d card and now has %d points %n", player.getPlayerName(), cardValue, player.score(cardValue));

        if(isBurst(player)){
            System.out.printf("Player %s burst %n",player.getPlayerName());
            isGameOver = true;
        }
        return false;
    }

    private static Player theWinnerIs(Player player, Player dealer){
        if(player.getScore() == 21 && dealer.getScore() == 21){
            return dealer;
        }
        return player.getScore() == 21 ? player : dealer.getScore() == 21 ? dealer : null;
    }

    private static boolean isBurst(Player player) {
        return player.score(player.cardValue) > HIGH_SCORE;
    }
}

