package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;
import academy.mindswap.p1g2.casino.server.utils.Messages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements Spot {
    ExecutorService executors = Executors.newFixedThreadPool(3);
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlerList;
    private List<WaitingRoom> waitingRooms;

    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.startServer(8080);
        server.acceptClient();
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientHandlerList = new ArrayList<>();
            waitingRooms = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(Messages.SERVER_RUNNING);
    }

    private void acceptClient() {
        System.out.println(Messages.SERVER_OPENING);
        try {

            Socket socket = serverSocket.accept();//blocking method
            // Create client
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clientHandlerList.add(clientHandler);
            new Thread(clientHandler).start();

            WaitingRoom waitingRoom;

            if ((clientHandlerList.size() - 1) % 3 == 0) {
                waitingRoom = new WaitingRoom(waitingRooms.size() + 1);
                waitingRooms.add(waitingRoom);
                executors.execute(waitingRoom);
            } else {
                waitingRoom = waitingRooms.get(waitingRooms.size() - 1);
            }

            waitingRoom.addClient(clientHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            acceptClient();
        }
    }

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
    public void listUsers(ClientHandler clientHandler) throws IOException {
        StringBuilder message = new StringBuilder();
        message.append("------------- USERS ---------------\n");
        clientHandlerList.forEach(clientHandler1 -> {
            if (!clientHandler1.equals(clientHandler)) {
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
