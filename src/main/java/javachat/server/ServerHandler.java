package javachat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private String client_username;

    ServerHandler(Socket client) {
        this.client = client;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    @Override
    public void run() {
        try {
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            client_username = input.readLine();
            System.out.println(client_username);

            String clientMessage = input.readLine();

            while (clientMessage != null) {
                if (input.readLine().startsWith("CLIENT:EXIT")) {
                    String[] parsed_message = clientMessage.split(":");
                    // sendMessage(parsed_message[1] + " has left the server");
                    shutdown();
                } else if (input.readLine().startsWith("CLIENT:NEW_USERNAME")) {
                    String[] parsed_message = clientMessage.split(":");
                    sendMessage(parsed_message[1] + " is now called " + parsed_message[2]);
                } else {
                    sendMessage(client_username + ":" + clientMessage);
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public void shutdown() throws IOException {
        try {
            input.close();
            output.close();

            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
}