package org.nhtoi.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.model.User;
import org.nhtoi.utils.APIHelper;
import twitter4j.auth.AccessToken;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) {
        displayMainView(primaryStage);
    }

    public static void displayMainView(Stage primaryStage) {
        AccessToken accessToken = OAuthManager.getCurrentUserToken();
        User authenticatedUser = APIHelper.fetchAuthenticatedUser(accessToken);

        if (authenticatedUser == null) {
            System.out.println("No authenticated user found.");
            AccountLinkingView.displayAccountLinkingView(primaryStage);
            return;
        }

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
            AccountLinkingView.displayAccountLinkingView(new Stage());
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameLabel, screenNameLabel, descriptionLabel, followingLabel, followersLabel, verifiedLabel, logoutButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Authenticated User Information");
        primaryStage.show();
    }
}
