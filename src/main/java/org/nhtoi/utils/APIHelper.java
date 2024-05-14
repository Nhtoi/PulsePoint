package org.nhtoi.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.nhtoi.model.User; // Assuming you have a User model class
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class APIHelper {
    private final Dotenv dotenv = Dotenv.load();
    private final String twitterApiKey = dotenv.get("TWITTER_API_KEY");
    private final String twitterApiKeySecret = dotenv.get("TWITTER_API_KEY_SECRET");
    private final String twitterAccessToken = dotenv.get("TWITTER_ACCESS_TOKEN");
    private final String twitterAccessTokenSecret = dotenv.get("TWITTER_ACCESS_TOKEN_SECRET");

    // Method to fetch authenticated user's information
    public User fetchAuthenticatedUser() {
        User authenticatedUser = null;
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(twitterApiKey)
                    .setOAuthConsumerSecret(twitterApiKeySecret)
                    .setOAuthAccessToken(twitterAccessToken)
                    .setOAuthAccessTokenSecret(twitterAccessTokenSecret);

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            // Retrieve authenticated user's information
            twitter4j.User twitterUser = twitter.verifyCredentials();
            // Create a User object from retrieved data
            authenticatedUser = new User(
                    twitterUser.getId(),
                    twitterUser.getScreenName(),
                    twitterUser.getName(),
                    twitterUser.getDescription(),
                    twitterUser.getProfileImageURL(),
                    twitterUser.isVerified(),
                    twitterUser.getFollowersCount(),
                    twitterUser.getStatusesCount(),
                    twitterUser.getFollowersCount(),
                    twitterUser.getFriendsCount()
            );

        } catch (TwitterException e) {
            e.printStackTrace();
            // Handle Twitter API exceptions here
        }
        return authenticatedUser;
    }
}
