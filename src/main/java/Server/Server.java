package Server;

import MainApplication.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.ServerSocket;


public class Server<tcpConnection> implements TCPListener {

    private final ObservableList<TCPConnection> connections = FXCollections.observableArrayList();
    private final Database conn;


    static final String DB_URL = "jdbc:mysql://localhost:3306/salers?" +
            "&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false" +
            "&serverTimezone=Europe/Moscow";
    static final String USER = "root";
    static final String PASSWORD = "@dmin001";


    public Server() {
        System.out.println("Server's running...");
        conn = new Database(DB_URL, USER, PASSWORD);

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
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

    public ObservableList<TCPConnection> getTCPConnections() {
        return connections;
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        Service.run(conn, tcpConnection, value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }
}
