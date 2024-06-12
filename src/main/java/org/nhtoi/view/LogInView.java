package org.nhtoi.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.utils.DatabaseHelper;
import org.nhtoi.utils.SessionManager;
import twitter4j.auth.AccessToken;

public class LogInView extends Application {
    @Override
    public void start(Stage primaryStage) {
        displayLogInView(primaryStage);
    }

    public static void displayLogInView(Stage primaryStage) {
        primaryStage.setTitle("Log In");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 0);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);

        Button loginButton = new Button("Log In");
        GridPane.setConstraints(loginButton, 1, 2);

        Button signUpButton = new Button("Sign Up");
        GridPane.setConstraints(signUpButton, 1, 3);

        loginButton.setOnAction(e -> {
            String email = emailInput.getText();
            String password = passwordInput.getText();
            int userId = DatabaseHelper.getUserIdByEmail(email);
            if (userId != -1) {
                // Check if the user has a saved access token
                try {
                    AccessToken accessToken = DatabaseHelper.loadAccessToken(userId);
                    if (accessToken != null) {
                        // Use the saved access token
                        OAuthManager.setCurrentUserId(String.valueOf(userId));
                        OAuthManager.setAccessToken(accessToken);
                        if (OAuthManager.isAuthenticated()) {
                            System.out.println("User authenticated using saved access token.");
                            SessionManager.setUserEmail(email); // Save email in SessionManager
                            MainView.setLoggedInUser(String.valueOf(userId));
                            MainView.displayMainView(primaryStage);
                        } else {
                            System.out.println("Saved access token is invalid.");
                            displayLogInView(primaryStage);
                        }
                    } else {
                        System.out.println("No saved access token found. Proceeding with normal login.");
                        SessionManager.setUserEmail(email); // Save email in SessionManager
                        MainView.setLoggedInUser(String.valueOf(userId));
                        MainView.displayMainView(primaryStage);
                    }
                } catch (Exception ex) {
                    System.out.println("Error checking for saved access token: " + ex.getMessage());
                    ex.printStackTrace();
                    displayLogInView(primaryStage);
                }
            } else {
                System.out.println("Invalid email or password.");
            }
        });

        signUpButton.setOnAction(e -> {
            SignUpView.displaySignUpView(primaryStage);
        });

        grid.getChildren().addAll(emailLabel, emailInput, passwordLabel, passwordInput, loginButton, signUpButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
