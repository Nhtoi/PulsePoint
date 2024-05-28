package org.nhtoi.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.nhtoi.model.User;
import org.nhtoi.utils.APIHelper;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class OAuthManager {
    private static final String CALLBACK_URL = "pulsepoint://callback";
    private static final Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\kevin\\IdeaProjects\\untitled\\src\\main\\java\\org\\nhtoi").load();
    private static final String twitterApiKey = dotenv.get("TWITTER_API_KEY");
    private static final String twitterApiKeySecret = dotenv.get("TWITTER_API_KEY_SECRET");
    private static final Map<String, AccessToken> userTokens = new HashMap<>();
    private static RequestToken requestToken;
    private static Twitter twitter;
    private static AccessToken currentUserToken;
    private static final String REQUEST_TOKEN_PATH = "C:\\Users\\kevin\\IdeaProjects\\untitled\\src\\main\\java\\org\\nhtoi\\auth\\requestToken.ser";
    private static final String CURRENT_USER_TOKEN_PATH = "C:\\Users\\kevin\\IdeaProjects\\untitled\\src\\main\\java\\org\\nhtoi\\auth\\currentUserToken.ser";

    static {
        loadCurrentUserToken();
        initializeTwitterInstance();
    }

    public static boolean isAuthenticated() {
        return currentUserToken != null;
    }

    public static boolean launchTwitterLinking() {
        try {
            if (twitter == null) {
                initializeTwitterInstance();
            }
            requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);

            System.out.println("RequestToken obtained: " + requestToken.getToken());
            saveRequestToken(requestToken);

            String authenticationURL = requestToken.getAuthorizationURL();
            openBrowser(authenticationURL);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void openBrowser(String url) throws IOException, URISyntaxException {
        System.out.println("Opening browser with URL: " + url);
        Desktop.getDesktop().browse(new URI(url));
    }

    public static void handleCallback(String callbackURI) {
        try {
            loadRequestToken(); // Ensure the requestToken is loaded

            // Ensure twitter instance is initialized
            if (twitter == null) {
                initializeTwitterInstance();
            }

            if (requestToken == null) {
                throw new IllegalStateException("requestToken is not initialized. Did you call launchTwitterLinking?");
            }

            String verifier = extractVerifierFromCallbackURI(callbackURI);
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            twitter.setOAuthAccessToken(accessToken);
            currentUserToken = accessToken;
            saveCurrentUserToken();
            userTokens.put(currentUserToken.getScreenName(), accessToken);

            User authenticatedUser = APIHelper.fetchAuthenticatedUser(accessToken);
            if (authenticatedUser != null) {
                System.out.println("User Information:");
                System.out.println("Screen Name: " + authenticatedUser.getScreenName());
            } else {
                System.out.println("Failed to retrieve user information.");
            }
        } catch (TwitterException e) {
            System.out.println("TwitterException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        currentUserToken = null;
        twitter.setOAuthAccessToken(null);
        saveCurrentUserToken();
    }

    public static AccessToken getCurrentUserToken() {
        return currentUserToken;
    }

    public static void setCurrentUser(String screenName) {
        currentUserToken = userTokens.get(screenName);
        if (currentUserToken != null) {
            twitter.setOAuthAccessToken(currentUserToken);
        }
    }

    public static Map<String, AccessToken> getUserTokens() {
        return userTokens;
    }

    private static void saveRequestToken(RequestToken requestToken) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REQUEST_TOKEN_PATH))) {
            oos.writeObject(requestToken);
            System.out.println("RequestToken saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadRequestToken() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REQUEST_TOKEN_PATH))) {
            requestToken = (RequestToken) ois.readObject();
            System.out.println("RequestToken loaded: " + requestToken.getToken());
        } catch (FileNotFoundException e) {
            System.out.println("requestToken.ser file not found. Proceeding without loading the request token.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveCurrentUserToken() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CURRENT_USER_TOKEN_PATH))) {
            oos.writeObject(currentUserToken);
            System.out.println("CurrentUserToken saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCurrentUserToken() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CURRENT_USER_TOKEN_PATH))) {
            currentUserToken = (AccessToken) ois.readObject();
            System.out.println("CurrentUserToken loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("currentUserToken.ser file not found. Proceeding without loading the current user token.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    private static void initializeTwitterInstance() {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(twitterApiKey, twitterApiKeySecret);
        if (currentUserToken != null) {
            twitter.setOAuthAccessToken(currentUserToken);
        }
    }
}
