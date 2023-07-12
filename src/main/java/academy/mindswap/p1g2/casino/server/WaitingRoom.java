package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.games.blackjack.Blackjack;
import academy.mindswap.p1g2.casino.server.games.manager.Game;
import academy.mindswap.p1g2.casino.server.games.manager.GameFactory;
import academy.mindswap.p1g2.casino.server.games.menu.MenuOption;
import academy.mindswap.p1g2.casino.server.games.poker.Poker;
import academy.mindswap.p1g2.casino.server.games.slotMachine.Slot;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.PlaySound;

import java.io.IOException;
import java.util.*;

public class WaitingRoom implements Runnable, Spot {
    private final List<ClientHandler> clientHandlerList;
    private final int number;
    private final PlaySound waitingSound;
    private final PlaySound gameStartSound;

    private final HashMap<MenuOption, Integer> votes;

    private Game game;

    public WaitingRoom(int number) {
        this.clientHandlerList = new ArrayList<>();
        this.number = number;
        gameStartSound = new PlaySound("../casino/sounds/game_start.wav");

        if (number == 1) {
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_1.wav");
        } else if (number == 2) {
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_2.wav");
        } else {
            waitingSound = new PlaySound("../casino/sounds/waiting_sound_3.wav");
        }
        votes = new HashMap<>();
    }

    public void init() throws InterruptedException, IOException {
        clientHandlerList.forEach(clientHandler -> {
            try {
                clientHandler.sendMessageUser(Messages.ROOM_STARTED);
                clientHandler.sendMessageUser(Messages.VOTE_MESSAGE);

                clientHandler.sendMessageUser(MenuOption.showMenu());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        synchronized (this) {
            wait();
        }

        waitingSound.stop();
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
            if (!isFull()) {
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
        clientHandler.sendMessageUser(String.format(Messages.ROOM_WAITING, number));
        if (isFull()) {
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
            if (!clientHandler1.equals(clientHandler)) {
                message.append(clientHandler1.getUsername()).append("\n");
            }
        });
        message.append(Messages.SEPARATOR);

        clientHandler.sendMessageUser(message.toString());
    }

    public void blackjack(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.BLACKJACK, 0);
        broadcast(String.format(Messages.NUMBER_OF_VOTES_BLACKJACK, count + 1), clientHandler);
        votes.put(MenuOption.BLACKJACK, count + 1);
        whisper(Messages.BLACKJACK_VOTE, clientHandler.getUsername());
        setGame(new Blackjack());
    }

    public void poker(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.POKER, 0);
        broadcast(String.format(Messages.NUMBER_OF_VOTES_POKER, count + 1), clientHandler);
        votes.put(MenuOption.POKER, count + 1);
        whisper(Messages.POKER_VOTE, clientHandler.getUsername());
        setGame(new Poker());
    }

    public void slots(ClientHandler clientHandler) {
        int count = votes.getOrDefault(MenuOption.SLOT_MACHINE, 0);
        broadcast(String.format(Messages.NUMBER_OF_VOTES_SLOTS, count + 1), clientHandler);
        votes.put(MenuOption.SLOT_MACHINE, count + 1);
        whisper(Messages.SLOTS_VOTE, clientHandler.getUsername());
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
    public void broadcast(String message, ClientHandler clientHandlerBroadcaster) {
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
