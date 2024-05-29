package org.nhtoi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
import twitter4j.auth.RequestToken;

public class DatabaseHelper {
    // Static connection instance
    private static Connection con = null;
    static Dotenv dotenv = Dotenv.configure()
            .directory("C:\\Users\\kevin\\OneDrive\\Desktop\\Java Projects\\PulsePoint\\src\\main\\java\\org\\nhtoi\\utils")
            .load();
    static String dbPassword = dotenv.get("DB_PASSWORD");

    public static Connection connectDB() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Pulsepoint",
                    "root", dbPassword);

            // Check if connection is successful
            if (con != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Failed to make connection!");
            }

            return con;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTokens(RequestToken requestToken) throws SQLException {
        if (con == null || con.isClosed()) {
            throw new SQLException("Database connection is not established.");
        }

        Statement stmt = con.createStatement();
        String sql = "INSERT INTO user (twitterToken) VALUES ('" + requestToken.getToken() + "')";
        stmt.executeUpdate(sql);
    }

    public static void main(String[] args) {
        // Test the connection
        Connection connection = connectDB();
        if (connection != null) {
            try {
                connection.close(); // Close the connection when done
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
