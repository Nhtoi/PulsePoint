package org.nhtoi.service;

import org.nhtoi.model.Notification;
import java.util.ArrayList;


public class NotificationService {
    ArrayList<Notification> notifications = new ArrayList<>();


    //Fetch notification (email, calendar, social media)
    public boolean getNotifications(Notification notifications){

        return true;
    }
    //add new notification (add fetched notification to a list)
    public void addNotification(Notification notification){

    }
    //mark notification as read (mark as read and delete notification from list)
    public boolean markNotificationAsRead(Notification notification){

        return true;
    }

}
