package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnUtil {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = DBPropertyUtil.getProperties("db.properties");
                if (props == null) {
                    System.out.println("DB properties file could not be loaded.");
                    return null;
                }

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                if (url == null || username == null || password == null) {
                    System.out.println("One or more DB properties are missing.");
                    return null;
                }

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connection successful.");
            } catch (SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
}

