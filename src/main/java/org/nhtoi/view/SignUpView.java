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
import org.nhtoi.utils.DatabaseHelper;

public class SignUpView extends Application {
    @Override
    public void start(Stage primaryStage) {
        displaySignUpView(primaryStage);
    }

    public static void displaySignUpView(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

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

        Button signUpButton = new Button("Sign Up");
        GridPane.setConstraints(signUpButton, 1, 2);

        Button backButton = new Button("Back to Log In");
        GridPane.setConstraints(backButton, 1, 3);

        signUpButton.setOnAction(e -> {
            String email = emailInput.getText();
            String password = passwordInput.getText();
            if (DatabaseHelper.createUser(email, password)) {
                System.out.println("User created successfully!");
                LogInView.displayLogInView(primaryStage);
            } else {
                System.out.println("Error creating user.");
            }
        });

        backButton.setOnAction(e -> {
            LogInView.displayLogInView(primaryStage);
        });

        grid.getChildren().addAll(emailLabel, emailInput, passwordLabel, passwordInput, signUpButton, backButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
