package controller;

import model.Notification;
import model.NotificationType;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NotificationController {
    private Map<String, List<Notification>> userNotifications;

    public NotificationController() {
        this.userNotifications = new HashMap<>();
    }

    public void sendNotification(String userId, String message, NotificationType type) {
        String notificationId = UUID.randomUUID().toString();
        Notification notification = new Notification(notificationId, userId, message, type);
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
        
        // Show notification in UI
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, 
                "New Notification", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public List<Notification> getUserNotifications(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return getUserNotifications(userId).stream()
                .filter(n -> !n.isRead())
                .toList();
    }

    public void markAsRead(String userId, String notificationId) {
        getUserNotifications(userId).stream()
                .filter(n -> n.getId().equals(notificationId))
                .forEach(n -> n.setRead(true));
    }

    public void markAllAsRead(String userId) {
        getUserNotifications(userId).forEach(n -> n.setRead(true));
    }
} 