package academy.mindswap.p1g2.casino.client;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    Socket socket;
    BufferedWriter socketWriter;
    BufferedReader consoleReader;

    public static void main(String[] args) throws IOException {
        TCPClient client = new TCPClient();
        client.startConsoleReader();
        client.handleServer();
    }

    private void startConsoleReader() {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void handleServer() {
        connectToServer();
        //   System.out.println("Connected");
        startListenToServer();
        //   System.out.println("Listening");
        communicateWithServer();
        //   System.out.println("After comm");
        close();
    }

    private void startListenToServer() {
        try {
            new Thread(new ServerListener(socket.getInputStream())).start();
        } catch (IOException e) {
            handleServer();
        }
    }

    private void communicateWithServer() {
        try {
            //   System.out.println("comm");
            sendMessages();
            communicateWithServer();
        } catch (IOException e) {
            System.out.println("Hum... seems that the server is dead");
            handleServer();
        }
    }


    private void sendMessages() throws IOException {
        String message = readFromConsole("\r");
        socketWriter.write(message);
        socketWriter.newLine();
        socketWriter.flush();

        if (message.equals("/quit")) {
            close();
            System.exit(0);
        }
    }


    private void connectToServer() {
        String hostName = "localhost"; //readFromConsole("Hello! What's the host of the server?");
        int port = 8080;//getPortNumber();
        try {
            this.socket = new Socket(hostName, port);
            this.socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Hum... seems that the server is dead");
            connectToServer();
        }
    }

    private int getPortNumber() {
        try {
            return Integer.parseInt(readFromConsole("And how about port to connect with"));
        } catch (NumberFormatException e) {
            System.out.println("that is not a number. Start counting 1, 2 , 3... these are numbers");
            return getPortNumber();
        }
    }

    private String readFromConsole(String question) {
        String message = null;
        System.out.println(question);
        try {
            message = consoleReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return message;
    }

    private void close() {
        try {
            System.out.println("Closing socket");
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private class ServerListener implements Runnable {
        BufferedReader serverReader;

        public ServerListener(InputStream inputStream) {
            this.serverReader = new BufferedReader(new InputStreamReader(inputStream));
        }

        @Override
        public void run() {
            try {
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readMessage() throws IOException {
            String readMessageFromServer = serverReader.readLine();
            System.out.println(readMessageFromServer);
            if (readMessageFromServer == null || readMessageFromServer.equals("quit")) {
                close();
                return;
            }

            readMessage();
        }
    }

}
