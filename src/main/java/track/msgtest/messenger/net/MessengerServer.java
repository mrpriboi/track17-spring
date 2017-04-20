package track.msgtest.messenger.net;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.*;

import track.msgtest.messenger.User;

/**
 *
 */
public class MessengerServer implements Runnable {
    private int serverPort = 9000;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private ArrayList<User> userTable = null;
    private Map<Long, OutputStream> userOnline = null;

    public MessengerServer(int port, ArrayList<User> table, Map<Long, OutputStream> base) {
        this.serverPort = port;
        this.userTable = table;
        this.userOnline = base;
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("Got the client");
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(
                    new Worker(clientSocket, userTable, userOnline)
            ).start();
        }
        System.out.println("Server Stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        System.out.println("Opening server socket...");
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    public static void main(String[] args) {
        final int PORT_WORK = 9000;
        final int PORT_STOP = 9001;
        User user1 = new User("admin", "password");
        User user2 = new User("user", "password");
        ArrayList<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        Map<Long, OutputStream> base = new HashMap<Long, OutputStream>();
        MessengerServer server = new MessengerServer(PORT_WORK, users, base);
        new Thread(server).start();
        try {
            Thread monitor = new StopMonitor(PORT_STOP);
            monitor.start();
            monitor.join();
            System.out.println("Right after join.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}

