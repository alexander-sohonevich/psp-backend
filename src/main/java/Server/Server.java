package Server;

import Entities.User;
import MainApplication.Service;
import UI_Application.Controller;
import UI_Application.Main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import static javafx.application.Application.launch;

public class Server implements TCPListener {

    private final ObservableList<TCPConnection> connections = FXCollections.observableArrayList();
    private final Database conn;
    //private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/Cars";
    static final String USER = "java_admin";
    static final String PASSWORD = "admin";


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
