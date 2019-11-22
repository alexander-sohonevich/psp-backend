package MainApplication;

import Server.Database;
import Server.TCPConnection;
import com.google.gson.Gson;

public class Admin {
    public static void admin(Database conn, TCPConnection tcpConnection) {
        while (tcpConnection.ping()) {
            String calledFunction = tcpConnection.receiveString();

            switch (calledFunction) {
                case "ADD USER":
                    addUser(conn, tcpConnection);
                    break;
                case "DELETE USER":
                    deleteUser(conn, tcpConnection);
                    break;
                case "UPDATE PASSWORD":
                    updateUserPassword(conn, tcpConnection);
                    break;
                case "VIEW ALL USERS":
                    viewAllUsers(conn, tcpConnection);
                    break;
            }

        }
    }


    private static void addUser(Database conn, TCPConnection tcpConnection) {
        Gson gson = new Gson();
        Entities.User user = gson.fromJson(tcpConnection.receiveString(), Entities.User.class);
        tcpConnection.sendString(user.addUser(conn));
    }

    private static void deleteUser(Database conn, TCPConnection tcpConnection) {
        Entities.User user = new Entities.User(tcpConnection.receiveString());
        tcpConnection.sendString(user.deleteUser(conn));
    }

    private static void updateUserPassword(Database conn, TCPConnection tcpConnection) {
        Entities.User user = new Entities.User(tcpConnection.receiveString());
        tcpConnection.sendString(user.updateUserPassword(conn));
    }

    private static void viewAllUsers(Database conn, TCPConnection tcpConnection) {
        Entities.User user = new Entities.User(tcpConnection.receiveString());
        tcpConnection.sendString(user.viewAllUsers(conn));
    }


}
