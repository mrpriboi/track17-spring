package track.msgtest.messenger.net;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import track.msgtest.messenger.User;

/**
 *
 */
public class MessengerServer implements Runnable {
    private final int serverPort = 9000;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private static Connection connection = null;
    private Map<Long, OutputStream> userOnline = null;

    public MessengerServer(Connection connection, Map<Long, OutputStream> base) {
        this.connection = connection;
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
                    new Worker(clientSocket,connection, userOnline)
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
        final int PORT_STOP = 9001;
        final String sql = "SELECT * FROM users;";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Messenger", "root", "Mysql97");
            Statement stmt = null;
            ResultSet rs = null;

            stmt = connection.createStatement();

            // 4) Набор "строк" таблицы - результат SELECT
            rs = stmt.executeQuery(sql);

            // 5) Структура ResultSet - получаем строки, пока есть
            while (rs.next()) {
            // Column index starts with 1
                Integer id = rs.getInt(1);          // 1 - ID
                String name = rs.getString("login"); // 2 - name
                String pass = rs.getString("password");     // 3 - age

            System.out.println(String.format("ID: %d, name: %s, password: %s", id, name, pass));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) {
            System.out.println("Unluck :( " + e.getMessage());
        }
        Map<Long, OutputStream> base = new HashMap<Long, OutputStream>();
        MessengerServer server = new MessengerServer(connection, base);
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

