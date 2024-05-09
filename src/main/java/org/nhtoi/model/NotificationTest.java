package org.nhtoi.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.nhtoi.service.NotificationService;
import java.util.Date;


public class NotificationTest {
    @Test
    public void testAddAndMarkNotification() {
        NotificationService service = new NotificationService();

        // Create a new notification
        Notification notification = new Notification("Title", "Message", new Date(), "Source", false);

        // Add notification to service
        service.addNotification(notification);

        // Verify that notification is added
        assertTrue(service.getNotifications(notification));

        // Mark notification as read
        service.markNotificationAsRead(notification);

        // Verify that notification is marked as read
        assertTrue(notification.isRead());
    }
}