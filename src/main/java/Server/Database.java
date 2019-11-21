package Server;

import java.sql.*;
import java.util.Scanner;

public class Database {
    private static Connection connection;
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public Database(String URL, String USER, String PASS) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            if (!connection.isClosed()) {
                connected = true;
                System.out.println("Connection to DB succeed!");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

    public int setConnection(String URL, String USER, String PASS) {
        try {
            Connection newCon = DriverManager.getConnection(URL, USER, PASS);
            if (!newCon.isClosed()) {
                connected = true;
                connection = newCon;
                System.out.println("Connection succeed!");
                return 1;
            }
        } catch (SQLException e) {
            return e.getErrorCode();
        }
        return 555;
    }

    public Connection getConnection() {
        return connection;
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void setAutoCommit(Boolean commit) {
        try {
            connection.setAutoCommit(commit);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
