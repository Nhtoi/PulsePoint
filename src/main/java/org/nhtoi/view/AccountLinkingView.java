package org.nhtoi.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nhtoi.auth.OAuthManager;

public class AccountLinkingView extends Application {

    @Override
    public void start(Stage primaryStage) {
        displayAccountLinkingView(primaryStage);
    }

    public static void displayAccountLinkingView(Stage primaryStage) {
        Button linkAccountButton = new Button("Link Twitter Account");
        linkAccountButton.setOnAction(event -> {
            if (!OAuthManager.launchTwitterLinking()) {
                System.out.println("Failed to launch Twitter linking process.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(linkAccountButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Link Your Twitter Account");
        primaryStage.show();
    }
}
