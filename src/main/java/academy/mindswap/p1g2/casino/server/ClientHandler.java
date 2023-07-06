package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.command.CommandInvoker;
import academy.mindswap.p1g2.casino.server.utils.Messages;
import academy.mindswap.p1g2.casino.server.utils.RandomNameGenerator;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;
    private String username;
    private String message;
    private final MessageSender messageSender;
    private static int numberOfClients = 0;

    public ClientHandler(Socket socket, TCPServer server) throws IOException {
        this.socket = socket;
        messageSender = new MessageSender(new CommandInvoker(server));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        numberOfClients++;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void changeSpot(Spot spot) {
        messageSender.changeSpot(spot);
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public static int getNumberOfClients() {
        return numberOfClients;
    }

    private void handleClient() throws IOException {
        sendMessageUser(Messages.INSERT_COMMAND);
        message = readMessageFromUser();
        messageSender.dealWithCommands(message, this);
        handleClient();
    }

    protected void sendMessageUser(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    private String readMessageFromUser() {
        try {
            String line = in.readLine(); //blocking method
            if (line == null) {
                socket.close();
                return "";
            }
            System.out.printf(Messages.CLIENT_MESSAGE, line);

            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void greetClient() throws IOException {
        System.out.println(Messages.CLIENT_ARRIVED);
        sendMessageUser(Messages.CLIENT_WELCOME);
        username = RandomNameGenerator.generateRandomName();
        sendMessageUser(String.format(Messages.CLIENT_NAME, username));
    }

    @Override
    public void run() {
        try {
            greetClient();
            handleClient();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
