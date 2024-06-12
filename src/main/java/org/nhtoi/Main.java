package org.nhtoi;

import javafx.application.Application;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.utils.DatabaseHelper;
import org.nhtoi.utils.SessionManager;
import org.nhtoi.view.LogInView;
import org.nhtoi.view.MainView;
import twitter4j.auth.RequestToken;

public class Main {
    public static void main(String[] args) {
        // Ensure database connection is established
        DatabaseHelper.connectDB();

        String currentUserEmail = SessionManager.getUserEmail();
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            System.out.println("No user email found. Please log in first.");
            Application.launch(LogInView.class, args);
            return;
        }

        int currentUserId = DatabaseHelper.getUserIdByEmail(currentUserEmail);
        if (currentUserId == -1) {
            // User does not exist, prompt to create a new account
            System.out.println("User not found. Please create an account.");
            Application.launch(LogInView.class, args);
            return;
        }

        OAuthManager.setCurrentUserId(String.valueOf(currentUserId));

        if (OAuthManager.isAuthenticated()) {
            System.out.println("User is authenticated.");
            Application.launch(MainView.class, args);
        } else if (args.length > 0) {
            String callbackURI = args[0];
            try {
                System.out.println("Handling callback with URI: " + callbackURI);
                OAuthManager.handleCallback(callbackURI);
                if (OAuthManager.isAuthenticated()) {
                    System.out.println("User authenticated after callback.");
                    Application.launch(MainView.class, args);
                } else {
                    System.out.println("Failed to authenticate user after callback.");
                    Application.launch(LogInView.class, args);
                }
            } catch (Exception e) {
                System.out.println("Error handling callback: " + e.getMessage());
                e.printStackTrace();
                Application.launch(LogInView.class, args);
            }
        } else {
            try {
                RequestToken existingToken = DatabaseHelper.loadRequestToken(currentUserId);
                if (existingToken != null) {
                    System.out.println("Existing request token found. Waiting for callback.");
                } else {
                    System.out.println("User is not authenticated and no callback URI provided. Starting the linking process.");
                    OAuthManager.launchTwitterLinking();
                }
            } catch (Exception e) {
                System.out.println("Error checking for existing request token: " + e.getMessage());
                e.printStackTrace();
            }
            Application.launch(LogInView.class, args);
        }
    }
}
