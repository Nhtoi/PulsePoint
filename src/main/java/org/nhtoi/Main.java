package org.nhtoi;

import javafx.application.Application;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.utils.DatabaseHelper;
import org.nhtoi.utils.SessionManager;
import org.nhtoi.view.LogInView;
import org.nhtoi.view.MainView;

public class Main {
    public static void main(String[] args) {
        // Ensure database connection is established
        DatabaseHelper.connectDB();

        if (args.length > 0) {
            String callbackURI = args[0];
            try {
                System.out.println("Handling callback with URI: " + callbackURI);
                // Get user email from SessionManager
                String currentUserEmail = SessionManager.getUserEmail();
                if (currentUserEmail == null || currentUserEmail.isEmpty()) {
                    System.out.println("No user email found. Please log in first.");
                    Application.launch(LogInView.class, args);
                    return;
                }
                int currentUserId = DatabaseHelper.getUserIdByEmail(currentUserEmail);
                OAuthManager.setCurrentUserId(String.valueOf(currentUserId));

                OAuthManager.handleCallback(callbackURI);
            } catch (IllegalStateException e) {
                System.out.println("Error handling callback: " + e.getMessage());
                e.printStackTrace();
                return; // Exit if handling callback fails
            }
        } else {
            System.out.println("No callback URI provided.");
        }

        if (OAuthManager.isAuthenticated()) {
            System.out.println("User is authenticated.");
            Application.launch(MainView.class, args);
        } else {
            System.out.println("User is not authenticated. Starting the linking process.");
            Application.launch(LogInView.class, args);
        }
    }
}
