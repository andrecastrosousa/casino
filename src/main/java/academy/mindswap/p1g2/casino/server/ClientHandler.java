package academy.mindswap.p1g2.casino.server;

import academy.mindswap.p1g2.casino.server.MessageSender;
import academy.mindswap.p1g2.casino.server.Spot;
import academy.mindswap.p1g2.casino.server.TCPServer;
import academy.mindswap.p1g2.casino.server.command.CommandInvoker;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
        private final Socket socket;
        private BufferedWriter out;
        private BufferedReader in;
        private String username;
        private String message;
        private final MessageSender messageSender;

        public ClientHandler(Socket socket, TCPServer server) throws IOException {
            this.socket = socket;
            messageSender = new MessageSender(new CommandInvoker(server));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        private void handleClient() throws IOException {
            sendMessageUser("Input your question");
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

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }

        void closeConnection() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void changeSpot(Spot spot) {
            messageSender.changeSpot(spot);
        }

}
