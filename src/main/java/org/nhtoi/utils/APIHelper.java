package org.nhtoi.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.List; // Import List
import org.nhtoi.model.Notification; // Import Notification
import java.util.ArrayList;

public class APIHelper {
    private final Dotenv dotenv = Dotenv.load();
    public final String twitterApiKey = dotenv.get("TWITTER_API_KEY");

    public List<Notification> fetchTwitterNotifications() {
        // Initialize an empty list of notifications
        List<Notification> fetchedNotifications = new ArrayList<>();

        try {
            // Implement API call to Twitter using the API key
            // Utilize an HTTP client library or Twitter API library

            // Parse the API response and convert it into a list of Notification objects
            // Add the notifications to the fetchedNotifications list

        } catch (Exception e) {
            // Handle any exceptions (e.g., network errors, parsing errors)
            e.printStackTrace();
        }

        // Return the list of fetched notifications
        return fetchedNotifications;
    }
}
