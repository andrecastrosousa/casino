package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;
import academy.mindswap.p1g2.casino.server.games.Game;
import academy.mindswap.p1g2.casino.server.games.GameFactory;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.menu.MenuOption;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;

import java.io.IOException;
import java.util.*;

public class WaitingRoom implements Runnable, Spot {
    private final List<ClientHandler> clientHandlerList;
    private int number;
    private PlaySound waitingSound;
    private PlaySound gameStartSound;

    private final HashMap<MenuOption, Integer> votes;

    private Game game;

    public WaitingRoom(int number) {
        this.clientHandlerList = new ArrayList<>();
        this.number = number;
        gameStartSound = new PlaySound("../casino/sounds/game_start.wav");

        if(number == 1){
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_1.wav");
        } else if (number == 2){
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_2.wav");
        } else {
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_3.wav");
        }
        votes = new HashMap<>();
    }

    public void init() throws InterruptedException, IOException {
        waitingSound.stop();
        clientHandlerList.forEach(clientHandler -> {
            try {
                clientHandler.sendMessageUser(Messages.ROOM_STARTED);
                clientHandler.sendMessageUser("Vote for a game to play...");

                clientHandler.sendMessageUser(MenuOption.showMenu());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        synchronized (this) {
            wait();
        }
        game.join(clientHandlerList);
        game.play();

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

    public void blackjack(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.BLACKJACK, 0);
        broadcast((count + 1) + " votes on blackjack", clientHandler);
        votes.put(MenuOption.BLACKJACK, count + 1);
        whisper("You've voted on Blackjack. \nWaiting for everyone to vote.",clientHandler.getUsername());
        setGame(new Blackjack());
    }

    public void poker(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.POKER, 0);
        broadcast((count + 1) + " votes on poker", clientHandler);
        votes.put(MenuOption.POKER, count + 1);
        whisper("You've voted on Poker. \nWaiting for everyone to vote.",clientHandler.getUsername());
        setGame(new Poker());
    }

    public void slots(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.SLOT_MACHINE, 0);
        broadcast((count + 1) + " votes on slots", clientHandler);
        votes.put(MenuOption.SLOT_MACHINE, count + 1);
        whisper("You've voted on Slots. \nWaiting for everyone to vote.",clientHandler.getUsername());
        setGame(new Slot());
    }

    public synchronized void setGame(Game game) {
        if (votes.values().stream().mapToInt(Integer::intValue).sum() == 3) {
            Map.Entry<MenuOption, Integer> mostVotedOption = votes.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);

            if (mostVotedOption != null) {
                this.game = GameFactory.getGameFromMenu(mostVotedOption.getKey());
            } else {
                this.game = game;
            }
            notifyAll();
        }
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
