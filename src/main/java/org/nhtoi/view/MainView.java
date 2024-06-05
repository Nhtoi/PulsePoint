package org.nhtoi.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.model.User;
import org.nhtoi.utils.APIHelper;
import twitter4j.auth.AccessToken;
import twitter4j.TwitterException;

public class MainView extends Application {
    private static String loggedInUser;

    public static void setLoggedInUser(String userId) {
        loggedInUser = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        displayMainView(primaryStage);
    }

    public static void displayMainView(Stage primaryStage) {
        AccessToken accessToken = OAuthManager.getCurrentUserToken();
        User authenticatedUser = null;

        if (accessToken != null) {
            authenticatedUser = APIHelper.fetchAuthenticatedUser(accessToken);
        }

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        if (authenticatedUser != null) {
            Label nameLabel = new Label("Name: " + authenticatedUser.getName());
            Label screenNameLabel = new Label("Screen Name: @" + authenticatedUser.getScreenName());
            Label descriptionLabel = new Label("Description: " + authenticatedUser.getDescription());
            Label followingLabel = new Label("Followers: " + authenticatedUser.getFollowersCounts());
            Label followersLabel = new Label("Following: " + authenticatedUser.getFriendsCount());
            Label verifiedLabel = new Label("Verified: " + authenticatedUser.isVerified());

            Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(event -> {
                OAuthManager.logout();
                primaryStage.close();
                LogInView.displayLogInView(new Stage());
            });

            layout.getChildren().addAll(nameLabel, screenNameLabel, descriptionLabel, followingLabel, followersLabel, verifiedLabel, logoutButton);
        } else {
            Label noAuthLabel = new Label("No authenticated user found.");
            Button linkTwitterButton = new Button("Link Twitter Account");
            linkTwitterButton.setOnAction(e -> {
                String userId = getLoggedInUserId(); // Implement this method to retrieve the logged-in user ID
                OAuthManager.setCurrentUserId(String.valueOf(Integer.parseInt(userId)));
                if (!OAuthManager.launchTwitterLinking()) {
                    System.out.println("Failed to launch Twitter linking process.");
                }
            });
            layout.getChildren().addAll(noAuthLabel, linkTwitterButton);
        }

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Authenticated User Information");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static String getLoggedInUserId() {
        return loggedInUser; // Return the logged-in user's ID as a String
    }
}
