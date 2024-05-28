package org.nhtoi;

import javafx.application.Application;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.view.MainView;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String callbackURI = args[0];
            try {
                System.out.println("Handling callback with URI: " + callbackURI);
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
            if (!OAuthManager.launchTwitterLinking()) {
                System.out.println("Failed to launch Twitter linking process.");
            }
        }
    }
}
