package org.nhtoi.utils;

import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class DatabaseHelper {
    private static Connection connection;

    public static void connectDB() {
        try {
            final Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\kevin\\OneDrive\\Desktop\\Java Projects\\PulsePoint\\src\\main\\java\\org\\nhtoi\\utils\\.env").load();
            final String password = dotenv.get("DB_PASSWORD");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pulsepoint", "root", password);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean createUser(String email, String password) {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot create user.");
            return false;
        }

        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User created with email: " + email);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static int getUserIdByEmail(String email) {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot get user ID.");
            return -1;
        }

        String query = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void saveRequestToken(int userId, RequestToken requestToken) throws SQLException {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot save request token.");
            throw new SQLException("No database connection.");
        }

        String query = "INSERT INTO request_tokens (user_id, twitterToken, twitterTokenSecret) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, requestToken.getToken());
            stmt.setString(3, requestToken.getTokenSecret());
            stmt.executeUpdate();
        }
    }

    public static RequestToken loadRequestToken(int userId) throws SQLException {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot load request token.");
            throw new SQLException("No database connection.");
        }

        String query = "SELECT twitterToken, twitterTokenSecret FROM request_tokens WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RequestToken(rs.getString("twitterToken"), rs.getString("twitterTokenSecret"));
            }
        }
        return null;
    }

    public static void saveAccessToken(int userId, AccessToken accessToken) throws SQLException {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot save access token.");
            throw new SQLException("No database connection.");
        }

        String query = "INSERT INTO access_tokens (user_id, twitterToken, twitterTokenSecret) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE twitterToken = VALUES(twitterToken), twitterTokenSecret = VALUES(twitterTokenSecret)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, accessToken.getToken());
            stmt.setString(3, accessToken.getTokenSecret());
            stmt.executeUpdate();
        }
    }

    public static AccessToken loadAccessToken(int userId) throws SQLException {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot load access token.");
            throw new SQLException("No database connection.");
        }

        String query = "SELECT twitterToken, twitterTokenSecret FROM access_tokens WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new AccessToken(rs.getString("twitterToken"), rs.getString("twitterTokenSecret"));
            }
        }
        return null;
    }
}
