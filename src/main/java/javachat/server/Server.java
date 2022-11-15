package javachat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ArrayList<ServerHandler> connections;
    private ServerSocket server;
    private boolean server_closed;
    private ExecutorService thread_pool;

    Server() {
        connections = new ArrayList<ServerHandler>();
        server_closed = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(4337);
            thread_pool = Executors.newCachedThreadPool();
            while (!server_closed) {
                Socket client = server.accept();
                ServerHandler client_handler = new ServerHandler(client);
                connections.add(client_handler);
                thread_pool.execute(client_handler);
            }
        } catch (IOException e) {
            try {
                shutdown();
            } catch (IOException ex) {
                System.out.println("Unsuccessful server shutdown.");
            }
        }
    }

    public void broadcast(String message) {
        for (ServerHandler connections:connections) {
            connections.sendMessage(message);
        }
    }

    public void shutdown() throws IOException {
        try {
            server_closed = true;
            if (!server.isClosed()) {
                server.close();
            }
            for (ServerHandler connection : connections) {
                connection.shutdown();
            }
        } catch (IOException e) {
            // ignore block
        }
    }
}
