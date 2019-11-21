package Entities;

import Server.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class User {
    private String login;
    private String password;
    private String userRole;

    private Boolean authorization;

    public User(String login) {
        this.login = login;
        this.password = "";
        this.userRole = "";
    }

    public User(String login, String password, String userRole) {
        this.login = login;
        this.password = password;
        this.userRole = userRole;
        this.authorization = false;
    }

    public void authorization(Database conn) {
        try {
            String query = "SELECT * FROM USERS " +
                    "WHERE LOGIN='" + login + "'";

            Statement statement = conn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                String loginDB = rs.getString("LOGIN");
                String passwordDB = rs.getString("PASSWORD");
                String userRoleDB = rs.getString("USER_ROLE");

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

    public String addUser(Database conn) {
        try {
            String checkQuery = "SELECT LOGIN FROM USERS WHERE LOGIN='" + this.login + "'";

            Statement statement = conn.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(checkQuery);

            while (rs.next()) {
                String loginDB = rs.getString("LOGIN");

                if (loginDB.equals(this.login)) {
                    statement.close();
                    rs.close();
                    return "Данный пользователь уже существует";
                }
            }

            String insertQuery = "INSERT INTO users " +
                    "VALUES ('%s','%s','%s')";

            insertQuery = String.format(insertQuery, this.login, this.password, this.userRole);

            Boolean check = statement.execute(insertQuery);

            statement.close();
            rs.close();

            if (!check) {
                return "Успешно";
            } else {
                return "Ошибка записи";
            }


        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

        return "";
    }

    public String deleteUser(Database conn) {
        try {
            String deleteQuery = "DELETE FROM USERS WHERE LOGIN='" + this.login + "'";
            Statement statement = conn.getConnection().createStatement();
            Boolean rs = statement.execute(deleteQuery);
            statement.close();

            if (!rs) {
                return "Успешно";
            } else {
                return "Ошибка удаления";
            }

        } catch (SQLException s) {
            System.err.println(s);
            s.setNextException(s);
        }

        return "Ошибка удаления";
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

    public Boolean getAuthorization() {
        return authorization;
    }
}




