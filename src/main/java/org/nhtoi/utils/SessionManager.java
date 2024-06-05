package org.nhtoi.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SessionManager {
    private static final String EMAIL_FILE = "C:\\Users\\kevin\\OneDrive\\Desktop\\Java Projects\\PulsePoint\\src\\main\\java\\org\\nhtoi\\utils\\user_email.txt";

    public static void setUserEmail(String email) {
        try (FileWriter writer = new FileWriter(EMAIL_FILE)) {
            writer.write(email);
        } catch (IOException e) {
            System.out.println("Error writing user email to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getUserEmail() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EMAIL_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading user email from file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void clearUserEmail() {
        try (FileWriter writer = new FileWriter(EMAIL_FILE)) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Error clearing user email file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
