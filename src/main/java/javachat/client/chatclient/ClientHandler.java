package javachat.client.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/*
    ClientHandler provides a socket and communication
    methods to and from a ChatServer
 */
public class ClientHandler {
    private String username;
    private Socket socket;
    private Boolean connected;
    private PrintWriter output;
    private BufferedReader input;
    private BufferedReader client_input;

    ClientHandler(String username) {
        this.username = username;
        this.connected = false;
    }

    public void connectToServer(String server_address) {
        if (!connected) {
            try {
                int server_port = 4337;
                socket = new Socket(server_address, server_port);
                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                client_input = new BufferedReader(new InputStreamReader(System.in));
                connected = true;
            }
            catch (IOException e) {
                System.out.println("Connection Failed.");
                e.printStackTrace();
            }
        }
    }

    public void disconnectFromServer() {
        if (connected) {
            try {
                socket = new Socket();
                output.print("CLIENT:EXIT:" + username);
                output.close();
                input.close();
                client_input.close();
                connected = false;
            } catch (IOException e) {
                // ignore block
            }
        }
    }

    public String getServerMessages() {
        String message = null;

        try {
            if (!Objects.equals(input.readLine(), "")) {
                message = input.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            // ignore block
        }

        return message;
    }

    public void sendMessage(String message) {
        if (connected) {
            output.println(message);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String new_username) {
        output.print("CLIENT:NEW_USERNAME:" + username + ":" + new_username);
        username = new_username;
    }

    public Boolean getConnectionStatus() {
        return connected;
    }
}