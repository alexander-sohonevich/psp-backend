package MainApplication;

import Entities.User;
import Server.Database;
import Server.TCPConnection;
import com.google.gson.Gson;
import org.json.JSONObject;

public class Service {
    private static User user;

    public static void run(Database conn, TCPConnection tcpConnection, String value) {
        Gson gson = new Gson();
        user = gson.fromJson(value, User.class);

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
