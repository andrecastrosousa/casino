package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.Commands;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements Spot {
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlerList;

    private List<Room> rooms;

    ExecutorService executors = Executors.newFixedThreadPool(3);

    final Object block = new Object();

    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.startServer(8080);
        server.acceptClient();
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientHandlerList = new ArrayList<>();
            rooms = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server is running");
    }

    private void acceptClient() {
        System.out.println("Server is accepting Clients");
        try {

            Socket socket = serverSocket.accept();//blocking method
            // Create client
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clientHandlerList.add(clientHandler);
            new Thread(clientHandler).start();

            Room room;

            if((clientHandlerList.size() - 1) % 3 == 0) {
                room = new Room(rooms.size() + 1);
                rooms.add(room);
                // new Thread(room).start();
                executors.execute(room);
                // new Thread(clientHandler).start();
            } else {
                room = rooms.get(rooms.size() - 1);
            }

            room.addClient(clientHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            acceptClient();
        }
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

    public void listCommands(ClientHandler clientHandler) throws IOException {
        clientHandler.sendMessageUser(Commands.listCommands());
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
        clientHandler.closeConnection();
    }
}
