package org.nhtoi.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.nhtoi.model.User;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class APIHelper {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("C:\\Users\\kevin\\OneDrive\\Desktop\\Java Projects\\PulsePoint\\src\\main\\java\\org\\nhtoi\\utils\\.env")
            .load();
    private static final String twitterApiKey = dotenv.get("TWITTER_API_KEY");
    private static final String twitterApiKeySecret = dotenv.get("TWITTER_API_KEY_SECRET");

    // Method to fetch authenticated user's information using a dynamic access token
    public static User fetchAuthenticatedUser(AccessToken dynamicAccessToken) {
        User authenticatedUser = null;
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(twitterApiKey)
                    .setOAuthConsumerSecret(twitterApiKeySecret);

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            twitter.setOAuthAccessToken(dynamicAccessToken);

            // Retrieve authenticated user's information
            twitter4j.User twitterUser = twitter.verifyCredentials();
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
