package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Player {
    private final ClientHandler clientHandler;
    private int currentBalance;
    private List<Card> cards;
    private Semaphore semaphore;

    public Player(ClientHandler clientHandler, Semaphore semaphore) {
        this.clientHandler = clientHandler;
        this.semaphore = semaphore;
        currentBalance = 100;
        cards = new ArrayList<>();
    }

    public void addBalance(int balance) {
        this.currentBalance += balance;
    }

    public int allIn() {
        currentBalance = 0;
        return currentBalance;
    }

    public void call(int amount) {
        currentBalance -= amount;
    }

    public void fold() {
        cards.clear();
    }

    public void raise(int amount) {
        currentBalance -= amount;
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public String showCards() {
        StringBuilder message = new StringBuilder();
        cards.forEach(card -> message.append(card.toString()));
        return message.toString();
    }

    public void playTurn() throws IOException {
        waitForTurn();
        clientHandler.sendMessageUser("Your turn. Please make a move."); // Send a message to the client

        String message = clientHandler.readMessageFromUser(); // Wait for the client's response

        // Process the client's message
        // For example, you can pass the message to the PokerGame instance
        clientHandler.getMessageSender().dealWithCommands(message, clientHandler);

        releaseTurn();
    }

    private void waitForTurn() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void releaseTurn() {
        semaphore.release();
    }
}
