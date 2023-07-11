package academy.mindswap.p1g2.casino.server.games;

import academy.mindswap.p1g2.casino.server.ClientHandler;

import java.io.IOException;

public abstract class PlayerImpl implements Player {
    protected final ClientHandler clientHandler;
    protected boolean isPlaying;
    protected int currentBalance;

    protected PlayerImpl(ClientHandler clientHandler, int currentBalance) {
        this.clientHandler = clientHandler;
        isPlaying = false;
        this.currentBalance = currentBalance;
    }
    protected PlayerImpl(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
        isPlaying = false;
        this.currentBalance = 0;
    }

    @Override
    public void sendMessage(String message) throws IOException {
        clientHandler.sendMessageUser(message);
    }

    @Override
    public void startTurn() {
        isPlaying = true;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void releaseTurn() {
        isPlaying = false;
    }

    @Override
    public void addBalance(int balance) {
        this.currentBalance += balance;
    }

    @Override
    public int getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
