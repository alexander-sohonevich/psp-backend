package MainApplication;

import Entities.User;
import Server.Database;
import Server.TCPConnection;
import org.json.JSONObject;

public class Service {
    private static User user;

    public static void run(Database conn, TCPConnection tcpConnection, String value) {
        JSONObject userJson = new JSONObject(value);

        user = new User(userJson.getString("login"), userJson.getString("password"),userJson.getString("userRole"));

        user.authorization(conn);

        if (user.getAuthorization()) {
            tcpConnection.sendString("1");
            switch (user.getUserRole()) {
                case ("Администратор"):
                    Admin.admin(conn, tcpConnection);
                    break;

                case ("Пользователь"):
                    MainApplication.User.user(conn, tcpConnection);
                    break;

            }
        }
        else {
            tcpConnection.sendString("0");
        }
    }

}
