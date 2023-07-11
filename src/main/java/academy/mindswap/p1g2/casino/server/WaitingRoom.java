package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaitingRoom implements Runnable, Spot {
    private final List<ClientHandler> clientHandlerList;
    private int number;
    private volatile boolean gameStarted;
    private PlaySound waitingSound;
    private PlaySound gameStartSound;

    public WaitingRoom(int number) {
        this.clientHandlerList = new ArrayList<>();
        gameStarted = false;
        this.number = number;
        gameStartSound = new PlaySound("../casino/sounds/game_start.wav");

        if(number == 1){
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_1.wav");
        } else if (number == 2){
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_2.wav");
        } else {
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_3.wav");
        }
    }

    public void init() throws InterruptedException, IOException {
        waitingSound.stop();
        clientHandlerList.forEach(clientHandler -> {
            try {
                clientHandler.sendMessageUser(Messages.ROOM_STARTED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        playGameStartSound();
        Slot slot = new Slot(clientHandlerList);
        slot.play();

    }
    private void playWaitingSound() {
        waitingSound.play();
    }
    private void playGameStartSound() {
        gameStartSound.play();
    }

    @Override
    public void run() {
        playWaitingSound();
        try {
            if(!isFull()) {
                synchronized (this) {
                    wait();
                }
            }
            init();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void addClient(ClientHandler clientHandler) throws IOException, InterruptedException {
        clientHandlerList.add(clientHandler);
        clientHandler.changeSpot(this);
        clientHandler.sendMessageUser(Messages.ROOM_WAITING);
        if(isFull()) {
            gameStarted = true;
            notifyAll();
        }
    }

    public boolean isFull() {
        return clientHandlerList.size() == 3;
    }

    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append(Messages.USER_SEPARATOR);
        clientHandlerList.forEach(clientHandler1 -> {
            if(!clientHandler1.equals(clientHandler)) {
                message.append(clientHandler1.getUsername()).append("\n");
            }
        });
        message.append(Messages.SEPARATOR);

        clientHandler.sendMessageUser(message.toString());
    }

    @Override
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

    @Override
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

    @Override
    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
        clientHandler.closeConnection();
    }
}
