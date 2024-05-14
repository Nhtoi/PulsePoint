package org.nhtoi.utils;

import org.junit.jupiter.api.Test;
import org.nhtoi.model.User;
import twitter4j.*;

public class APIHelperTest {

    @Test
    void testFetchAuthenticatedUser() throws TwitterException {

        // Create APIHelper instance
        APIHelper apiHelper = new APIHelper();

        // Call the method to be tested
        User authenticatedUser = apiHelper.fetchAuthenticatedUser();

        // Log the retrieved information using JUnit's built-in logging
        System.out.println("Authenticated User Information:");
        System.out.println("ID: " + authenticatedUser.getId());
        System.out.println("Screen Name: " + authenticatedUser.getScreenName());
        System.out.println("Name: " + authenticatedUser.getName());
        System.out.println("Description: " + authenticatedUser.getDescription());
        System.out.println("Profile Image URL: " + authenticatedUser.getProfileImageUrl());
        System.out.println("Followers Count: " + authenticatedUser.getFollowersCounts());
        System.out.println("Verified: " + authenticatedUser.isVerified());
        System.out.println("Friends Count: " + authenticatedUser.getFriendsCount());
        System.out.println("Statuses Count: " + authenticatedUser.getTweetCount());

    }
}
