package org.nhtoi.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.nhtoi.utils.DatabaseHelper;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OAuthManager {
    private static final String CALLBACK_URL = "pulsepoint://callback";
    private static final Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\kevin\\OneDrive\\Desktop\\Java Projects\\PulsePoint\\src\\main\\java\\org\\nhtoi\\utils\\.env").load();
    private static final String twitterApiKey = dotenv.get("TWITTER_API_KEY");
    private static final String twitterApiKeySecret = dotenv.get("TWITTER_API_KEY_SECRET");
    private static final Map<String, AccessToken> userTokens = new HashMap<>();
    private static RequestToken requestToken;
    private static Twitter twitter;
    private static AccessToken currentUserToken;
    private static String currentUserId;

    static {
        initializeTwitterInstance();
    }

    public static boolean isAuthenticated() {
        return currentUserToken != null;
    }

    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
    }
    public static void setAccessToken(AccessToken accessToken) {
        currentUserToken = accessToken;
        if (twitter != null) {
            twitter.setOAuthAccessToken(accessToken);
        }
    }
    public static boolean launchTwitterLinking() {
        try {
            if (twitter == null) {
                initializeTwitterInstance();
            }
            requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);

            System.out.println("RequestToken obtained: " + requestToken.getToken());

            // Save the request token to the database for the current user
            DatabaseHelper.connectDB();
            DatabaseHelper.saveRequestToken(Integer.parseInt(currentUserId), requestToken);

            String authenticationURL = requestToken.getAuthorizationURL();
            openBrowser(authenticationURL);

            return true;
        } catch (TwitterException e) {
            System.out.println("TwitterException: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("URISyntaxException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static void openBrowser(String url) throws IOException, URISyntaxException {
        System.out.println("Opening browser with URL: " + url);
        Desktop.getDesktop().browse(new URI(url));
    }

    public static void handleCallback(String callbackURI) {
        try {
            requestToken = DatabaseHelper.loadRequestToken(Integer.parseInt(currentUserId));
            if (requestToken == null) {
                throw new IllegalStateException("requestToken is not initialized. Did you call launchTwitterLinking?");
            }

            if (twitter == null) {
                initializeTwitterInstance();
            }

            String verifier = extractVerifierFromCallbackURI(callbackURI);
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            twitter.setOAuthAccessToken(accessToken);
            currentUserToken = accessToken;

            // Save the current user token to the database
            DatabaseHelper.saveAccessToken(Integer.parseInt(currentUserId), currentUserToken);
            userTokens.put(currentUserId, accessToken);

            System.out.println("Successfully authenticated and obtained access token for user " + currentUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        currentUserToken = null;
        if (twitter != null) {
            twitter.setOAuthAccessToken(null);
        }
        // Optionally, remove the current user token from the database
    }

    public static AccessToken getCurrentUserToken() {
        return currentUserToken;
    }

    public static void setCurrentUser(String userId) {
        try {
            currentUserToken = DatabaseHelper.loadAccessToken(Integer.parseInt(userId));
            if (currentUserToken != null) {
                if (twitter == null) {
                    initializeTwitterInstance();
                }
                twitter.setOAuthAccessToken(currentUserToken);
                System.out.println("Access token loaded and set for user: " + userId);
            } else {
                System.out.println("No access token found for user: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, AccessToken> getUserTokens() {
        return userTokens;
    }

    private static String extractVerifierFromCallbackURI(String callbackURI) {
        System.out.println("Extracting verifier from callback URI: " + callbackURI);
        String[] urlParts = callbackURI.split("\\?");
        if (urlParts.length > 1) {
            String[] queryParams = urlParts[1].split("&");
            for (String param : queryParams) {
                if (param.startsWith("oauth_verifier=")) {
                    return param.split("=")[1];
                }
            }
            throw new IllegalArgumentException("oauth_verifier not found in the callback URI: " + callbackURI);
        } else {
            throw new IllegalArgumentException("Invalid callback URI: " + callbackURI);
        }
    }
    private static String parseAccessToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("access_token")) {
                return jsonObject.getString("access_token");
            } else {
                throw new IllegalArgumentException("Access token not found in the response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void initializeTwitterInstance() {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(twitterApiKey, twitterApiKeySecret);
        if (currentUserToken != null) {
            twitter.setOAuthAccessToken(currentUserToken);
        }
    }
}
