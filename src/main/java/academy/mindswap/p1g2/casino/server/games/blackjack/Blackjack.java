package academy.mindswap.p1g2.casino.server.games.blackjack;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;
import academy.mindswap.p1g2.casino.server.games.poker.command.BetOption;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rules:
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

public class Blackjack implements Spot {
    public static final int HIGH_SCORE = 21;
    private Dealer dealer;
    private List<Card> cards;
    private List<Player> players;
    private int currentPlayerPlaying;
    private int handCount;
    private PlaySound hitSound;
    private PlaySound winSound;

    public Blackjack(List<ClientHandler> clientHandlerList) {
        players = new ArrayList<>();
        cards = DeckGenerator.getDeckOfCards();
        dealer = new Dealer(cards);
        hitSound = new PlaySound("../casino/sounds/hit_sound.wav");
        winSound = new PlaySound("../casino/sounds/you_win_sound.wav");
        clientHandlerList.forEach(clientHandler -> {
            players.add(new Player(clientHandler));
            clientHandler.changeSpot(this);
        });
        handCount = 0;
        currentPlayerPlaying = 0;
    }
    private void playWinSound() {
        winSound.play();
    }
    public void play() throws IOException, InterruptedException {

        while (!gameOver()) {
            dealer.shuffle();
            dealer.giveCards(players);
            List<Player> playersNotBurst = players.stream().filter(player -> player.getScore() > 0).toList();
            while (currentPlayerPlaying < playersNotBurst.size()) {
                Player currentPlayer = playersNotBurst.get(currentPlayerPlaying);
                broadcast(String.format(Messages.SOMEONE_PLAYING, currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
                currentPlayer.getClientHandler().sendMessageUser(Messages.YOUR_TURN);
                currentPlayer.startTurn();

                while (currentPlayer.isPlaying()) {
                }
                currentPlayerPlaying++;
            }
            currentPlayerPlaying = 0;
            handCount++;
            theWinnerIs();
            dealer.resetHand();
        }


    }
    private void playHitSound() {
        hitSound.play();
    }
    public void hit(ClientHandler clientHandler) throws IOException {
        playHitSound();
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.receiveCard(dealer.giveCard());
        if (currentPlayer.getScore() > HIGH_SCORE) {
            dealer.receiveCardsFromPlayer(currentPlayer.returnCards(Messages.HAND_BURST));
        } else {
            clientHandler.sendMessageUser(currentPlayer.showCards());
        }
        currentPlayer.releaseTurn();
    }

    public void stand(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        currentPlayer.releaseTurn();
    }

    public void surrender(ClientHandler clientHandler) throws IOException {
        Player currentPlayer = players.get(currentPlayerPlaying);
        if (!currentPlayer.getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser(Messages.NOT_YOUR_TURN);
            return;
        }
        dealer.receiveCardsFromPlayer(currentPlayer.returnCards(Messages.YOU_GIVE_UP));
        currentPlayer.releaseTurn();
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster) {
        players.stream()
                .filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
                .forEach(player -> {
                    try {
                        player.getClientHandler().sendMessageUser(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void whisper(String message, String clientToSend) {
        players.stream()
                .filter(player -> Objects.equals(player.getClientHandler().getUsername(), clientToSend))
                .forEach(player -> {
                    try {
                        player.sendMessageToPlayer(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
        clientHandler.sendMessageUser(BetOption.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if (playerToRemove != null) {
            players.remove(playerToRemove);
            playerToRemove.getClientHandler().closeConnection();
        }
    }

    private Player getPlayerByClient(ClientHandler clientHandler) {
        return players.stream()
                .filter(player -> player.getClientHandler().equals(clientHandler))
                .findFirst()
                .orElse(null);
    }

    private boolean gameOver() {
        return handCount > 3;
    }

    private void theWinnerIs() throws IOException {
        broadcast(dealer.showCards(), players.get(0).getClientHandler());
        players.get(0).sendMessageToPlayer(dealer.showCards());
        List<Player> players = this.players.stream().filter(player -> player.getScore() > 0).toList();
        players.forEach(player -> {
            try {
                if (dealer.getScore() > player.getScore() && dealer.getScore() <= 21) {
                    dealer.receiveCardsFromPlayer(player.returnCards(Messages.DEALER_WIN));
                } else {
                    playWinSound();
                    dealer.receiveCardsFromPlayer(player.returnCards(Messages.YOU_WIN));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}


