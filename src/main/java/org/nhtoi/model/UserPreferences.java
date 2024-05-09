package org.nhtoi.model;

public class UserPreferences {

    private String preferredLanguage;
    private boolean playSound;
    private boolean showPopups;

    public UserPreferences(String language, boolean playSound, boolean showPopups) {
        this.preferredLanguage = language;
        this.playSound = playSound;
        this.showPopups = showPopups;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public boolean isShowPopups() {
        return showPopups;
    }

    public void setShowPopups(boolean showPopups) {
        this.showPopups = showPopups;
    }
}
