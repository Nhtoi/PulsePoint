package org.nhtoi.service;

import org.nhtoi.model.Notification;
import org.nhtoi.utils.APIHelper;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final APIHelper apiHelper = new APIHelper();
    private final List<Notification> notifications = new ArrayList<>();

    // Fetch notifications from email, calendar, and social media
    public boolean getNotifications() {
        try {
            // Fetch notifications from Twitter using APIHelper
            List<Notification> twitterNotifications = apiHelper.fetchTwitterNotifications();

            // Add fetched notifications to the list
            notifications.addAll(twitterNotifications);

            // Return true on successful fetch
            return true;
        } catch (Exception e) {
            // Handle exceptions (e.g., API failures, parsing errors)
            e.printStackTrace();
            return false;
        }
    }

    // Add a new notification to the list
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    // Mark a notification as read and remove it from the list
    public boolean markNotificationAsRead(Notification notification) {
        if (notifications.contains(notification)) {
            // Mark notification as read (implementation depends on Notification class)
            notification.setRead(true);
            // Remove the notification from the list
            notifications.remove(notification);
            return true;
        }
        return false;
    }
}
