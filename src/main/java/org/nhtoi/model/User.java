package org.nhtoi.model;

public class User {
    public long id;
    public String screenName;
    public String name;
    public String description;
    public String profileImageUrl;
    public boolean isVerified;
    public int followingCount;
    public int tweetCount;
    public int followersCounts;
    public int friendsCount;

    public User(long id, String screenName, String name, String description, String profileImageUrl, boolean isVerified, int followingCount, int tweetCount, int followersCounts, int friendsCount) {
        this.id = id;
        this.screenName = screenName;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.isVerified = isVerified;
        this.followingCount = followingCount;
        this.tweetCount = tweetCount;
        this.followersCounts = followersCounts;
        this.friendsCount = friendsCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public int getFollowersCounts() {
        return followersCounts;
    }

    public void setFollowersCounts(int followersCounts) {
        this.followersCounts = followersCounts;
    }
}
