package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final ClientHandler clientHandler;
    private volatile boolean isPlaying;
    private int currentBalance;
    SlotMachine slotmachine;

    public Player (ClientHandler clientHandler, SlotMachine slotMachine) {
        this.clientHandler = clientHandler;
        currentBalance = 2;
        isPlaying = false;
        this.slotmachine = slotMachine;
        this.slotmachine.sitPlayer(this);
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void startTurn() {
        isPlaying = true;
    }

    public void releaseTurn() {
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    public void addBalance(int balance) {
        this.currentBalance += balance;
    }
    public void sendMessage(String message) throws IOException {
        clientHandler.sendMessageUser(message);

    }

    public void doubleBet() throws IOException, InterruptedException {
        slotmachine.setBet(2);
        slotmachine.play();
    }

    public void spin() throws IOException, InterruptedException {
        slotmachine.setBet(1);
        slotmachine.play();
    }

    public int getBalance() {
        return currentBalance;
    }
}
