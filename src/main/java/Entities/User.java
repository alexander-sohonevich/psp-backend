package Entities;

import Server.Database;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
    private String login;
    private String password;
    private String userRole;

    private Boolean authorization;

    public User(String login, String password, String userRole) {
        this.login = login;
        this.password = password;
        this.userRole = userRole;
        this.authorization = false;
    }

    public void authorization(Database conn) {
        try {
            String query = "SELECT * FROM public.\"USERS\"";

            Statement statement = conn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                String loginDB = rs.getString("login");
                String passwordDB = rs.getString("password");
                String userRoleDB = rs.getString("userRole");

                if (validation(loginDB, passwordDB, userRoleDB)) {
                    authorization = true;
                }
            }

            statement.close();
            rs.close();
        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

    }

    private Boolean validation(String loginDB, String passwordDB, String userRoleDB) {
        if (login.equals(loginDB) && password.equals(passwordDB) && userRole.equals(userRoleDB)) {
            return true;
        }
        return false;
    }

    public String getUserRole() {
        return userRole;
    }

    public Boolean getAuthorization() {
        return authorization;
    }
}




