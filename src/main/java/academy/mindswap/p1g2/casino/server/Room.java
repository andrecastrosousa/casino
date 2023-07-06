package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;
<<<<<<< HEAD
import academy.mindswap.p1g2.casino.server.games.slotMachine.SlotMachine;
=======
import academy.mindswap.p1g2.casino.server.games.Card;
import academy.mindswap.p1g2.casino.server.games.DeckGenerator;
import academy.mindswap.p1g2.casino.server.utils.Messages;
>>>>>>> d7d03d5 (Added Poker commands and rules)

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room implements Runnable, Spot {
    private final List<ClientHandler> clientHandlerList;

    private int number;

    private volatile boolean gameStarted;

    public Room(int number) {
        this.clientHandlerList = new ArrayList<>();
        gameStarted = false;
        this.number = number;
    }

    public void init() throws InterruptedException {

        clientHandlerList.forEach(clientHandler -> {
            try {
                clientHandler.sendMessageUser("Game will start soon...");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void run() {
        try {
            if(!isFull()) {
                synchronized (this) {
                    wait();
                }
            }
            init();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void addClient(ClientHandler clientHandler) throws IOException, InterruptedException {
        clientHandlerList.add(clientHandler);
        clientHandler.changeSpot(this);
        clientHandler.sendMessageUser("\nWaiting for players to start the game...\n");
        if(isFull()) {
            gameStarted = true;
            notifyAll();
        }
    }

    public boolean isFull() {
        return clientHandlerList.size() == 3;
    }

    public void broadcast(String message, ClientHandler clientHandlerBroadcaster){
        clientHandlerList
                .stream().filter(clientHandler -> !clientHandlerBroadcaster.equals(clientHandler))
                .forEach(clientHandler -> {
                    try {
                        clientHandler.sendMessageUser(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void whisper(String message, String clientToSend) {
        clientHandlerList.stream()
                .filter(clientHandler -> Objects.equals(clientHandler.getUsername(), clientToSend))
                .forEach(clientHandler -> {
                    try {
                        clientHandler.sendMessageUser(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        clientHandlerList.forEach(clientHandler1 -> {
            if(!clientHandler1.equals(clientHandler)) {
                message.append(clientHandler1.getUsername()).append("\n");
            }
        });
        message.append("-----------------------------------");

        clientHandler.sendMessageUser(message.toString());
    }

    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
        clientHandler.closeConnection();
    }
}
