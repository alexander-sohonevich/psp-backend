package Server;

import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final Database conn;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    //private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/Cars";
    static final String USER = "java_admin";
    static final String PASSWORD = "admin";

    private Server() {
        System.out.println("Server's running...");
        conn = new Database(DB_URL, USER, PASSWORD);

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
       Server s = new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.charAt(0) == 'U')
            System.out.println(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TCPConnection c : connections) c.sendString(value);
    }

}
