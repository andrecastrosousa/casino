package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.poker.command.PokerCommands;
import academy.mindswap.p1g2.casino.server.games.poker.rule.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Poker implements Spot {
    private final Evaluator evaluatorChain;
    private final List<Player> players;
    private final List<Player> playersPlaying;
    private final Dealer dealer;
    private int balance;
    private int currentPlayerPlaying;

    public Poker(List<ClientHandler> clientHandlers) {
        this.dealer = new Dealer();
        evaluatorChain = new RoyalFlushEvaluator();
        players = new ArrayList<>();
        playersPlaying = new ArrayList<>();

        clientHandlers.forEach(clientHandler -> {
            Player player = new Player(clientHandler);
            players.add(player);
            playersPlaying.add(player);
            clientHandler.changeSpot(this);
        });
        createEvaluatorChain();
        balance = 0;
        currentPlayerPlaying = 0;
    }

    private void createEvaluatorChain() {
        evaluatorChain.setNextEvaluator(new StraightFlushEvaluator());
        evaluatorChain.setNextEvaluator(new FourOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new FullHouseEvaluator());
        evaluatorChain.setNextEvaluator(new FlushEvaluator());
        evaluatorChain.setNextEvaluator(new StraightEvaluator());
        evaluatorChain.setNextEvaluator(new ThreeOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new TwoPairEvaluator());
        evaluatorChain.setNextEvaluator(new OnePairEvaluator());
        evaluatorChain.setNextEvaluator(new HighCardEvaluator());
    }

    private Player getPlayerByClient(ClientHandler clientHandler) {
        return players.stream().filter(player -> player.getClientHandler().equals(clientHandler)).findFirst().orElse(null);
    }

    public void finishHand() {
        evaluatorChain.evaluateHand();
    }

    public synchronized void play() throws IOException, InterruptedException {
        dealer.shuffle();
        dealer.distributeCards(players);

        while (!gameEnded()) {
            Player currentPlayer = playersPlaying.get(currentPlayerPlaying);
            broadcast(String.format("%s is playing. Wait for your turn.", currentPlayer.getClientHandler().getUsername()), currentPlayer.getClientHandler());
            currentPlayer.getClientHandler().sendMessageUser("It is your time to play.");
            wait();
            synchronized (this) {
                currentPlayerPlaying = (currentPlayerPlaying + 1) % playersPlaying.size();
                notifyAll();
            }
        }
    }

    public synchronized void waitForTurn(Player player) {
        while (players.get(currentPlayerPlaying) != player) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
    }

    private boolean gameEnded() {
        return players.size() <= 1;
    }

    public void allIn(ClientHandler clientHandler) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        balance += player.allIn();
        player.playTurn(this);

    }

    public void call(ClientHandler clientHandler, int amount) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        balance += amount;
        player.playTurn(this);
    }

    public void check(ClientHandler clientHandler) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.playTurn(this);
    }

    public void fold(ClientHandler clientHandler) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        playersPlaying.remove(player);
        player.playTurn(this);
    }

    public void raise(ClientHandler clientHandler, int amount) throws IOException {
        if(!players.get(currentPlayerPlaying).getClientHandler().equals(clientHandler)) {
            clientHandler.sendMessageUser("Isn't your time to play.");
            return;
        }
        Player player = getPlayerByClient(clientHandler);
        player.raise(amount);
        player.playTurn(this);
    }

    @Override
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster){
        players
                .stream().filter(player -> !clientHandlerBroadcaster.equals(player.getClientHandler()))
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
                        player.getClientHandler().sendMessageUser(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
        clientHandler.sendMessageUser(PokerCommands.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        Player playerToRemove = getPlayerByClient(clientHandler);
        if(playerToRemove != null) {
            players.remove(playerToRemove);
            playerToRemove.getClientHandler().closeConnection();
        }
    }
}
