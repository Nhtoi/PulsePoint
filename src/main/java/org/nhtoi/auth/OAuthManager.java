package org.nhtoi.auth;
import io.github.cdimascio.dotenv.Dotenv;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuthManager extends HttpServlet {

    private static final String CALLBACK_URL = "http://localhost:8080/callback"; // Change to your callback URL
    private static final Dotenv dotenv = Dotenv.load();

    private final String twitterApiKey = dotenv.get("TWITTER_API_KEY");
    private final String twitterApiKeySecret = dotenv.get("TWITTER_API_KEY_SECRET");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(twitterApiKey, twitterApiKeySecret);

        try {
            // Step 1: Get request token
            RequestToken requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
            req.getSession().setAttribute("requestToken", requestToken);

            // Step 2: Redirect user to Twitter's authentication URL
            String authenticationURL = requestToken.getAuthorizationURL();
            resp.sendRedirect(authenticationURL);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to authenticate with Twitter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oauthVerifier = req.getParameter("oauth_verifier");
        RequestToken requestToken = (RequestToken) req.getSession().getAttribute("requestToken");

        if (oauthVerifier != null && requestToken != null) {
            try {
                Twitter twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(twitterApiKey, twitterApiKeySecret);

                // Step 3: Get access token using the request token and verifier
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);

                // Step 4: Store access token securely (e.g., in database)
                // accessToken.getToken() and accessToken.getTokenSecret()

                // Step 5: Make authenticated requests to Twitter API using accessToken

                // Redirect user to the homepage or other desired page
                resp.sendRedirect("/home");
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to obtain access token");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing oauth_verifier parameter");
        }
    }
}

