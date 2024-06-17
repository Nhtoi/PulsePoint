package org.nhtoi.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nhtoi.auth.OAuthManager;
import org.nhtoi.utils.APIHelper;
import twitter4j.auth.AccessToken;

public class PostManagerView {

    public static void displayPostManagerView(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label postLabel = new Label("Create a new post:");
        TextArea postTextArea = new TextArea();
        postTextArea.setPromptText("What's happening?");
        Button postButton = new Button("Post to Twitter");

        postButton.setOnAction(event -> {
            String tweetText = postTextArea.getText();
            if (!tweetText.isEmpty()) {
                postToTwitter(tweetText);
                primaryStage.close();
                MainView.displayMainView(new Stage());
            } else {
                System.out.println("Tweet cannot be empty.");
            }
        });

        layout.getChildren().addAll(postLabel, postTextArea, postButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Post");
        primaryStage.show();
    }

    private static void postToTwitter(String tweetText) {
        AccessToken accessToken = OAuthManager.getCurrentUserToken();
        APIHelper.postTweet(tweetText, accessToken);
    }
}
