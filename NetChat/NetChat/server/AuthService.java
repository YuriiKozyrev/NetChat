package NetChat.server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String setNewUsers(int id, String login, String pass, String nickname) {
        int hash = pass.hashCode();
        String sql = String.format("INSERT INTO main (id, login, password, nickname) VALUES ('%s', '%s', '%s', '%s')", id, login, hash, nickname);
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, pass);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String addNickToBlackList(String nickGood, String nickBad){
        String str;
        if(isNickInBlackList(nickGood, nickBad)){
            str = (nickBad + " уже в вашем черном списке");
        } else {
            String sql = String.format("INSERT INTO blacklist (nickGood, nickBad) VALUES ('%s', '%s')", nickGood, nickBad);
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            str = ("Вы добавили пользователя " + nickBad + " в черный список");
        }
        return str;
    }

    public static boolean isNickInBlackList(String nickTo, String nickFrom) {
        String query1 = String.format("SELECT * FROM blacklist where nickGood = '%s'", nickTo);
        try {
            ResultSet rs = stmt.executeQuery(query1);
            while (rs.next()) {
                String str = rs.getString(2);
                if(nickFrom.equals(str)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
