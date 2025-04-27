package view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import controller.NotificationController;
import model.Notification;
import controller.UserController;

public class NotificationPanel extends JPanel {
    private NotificationController notificationController;
    private JPanel notificationsContainer;
    private Timer refreshTimer;
    private UserController userController;

    public NotificationPanel(NotificationController notificationController, UserController userController) {
        this.notificationController = notificationController;
        this.userController = userController;
        setupUI();
        startAutoRefresh();
    }

    private void setupUI() {
        setPreferredSize(new Dimension(250, 0));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(200, 200, 200)));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton markAllReadButton = new JButton("Mark All Read");
        styleButton(markAllReadButton);
        markAllReadButton.addActionListener(e -> markAllAsRead());
        headerPanel.add(markAllReadButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Notifications Container
        notificationsContainer = new JPanel();
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(notificationsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);

        refreshNotifications();
    }

    private void refreshNotifications() {
        notificationsContainer.removeAll();
        String userId = userController.getCurrentUser().getId();
        
        for (Notification notification : notificationController.getUserNotifications(userId)) {
            JPanel notificationCard = createNotificationCard(notification);
            notificationsContainer.add(notificationCard);
        }
        
        notificationsContainer.revalidate();
        notificationsContainer.repaint();
    }

    private JPanel createNotificationCard(Notification notification) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel messageLabel = new JLabel(notification.getMessage());
        JLabel timeLabel = new JLabel(notification.getTimestamp().toString());
        
        card.add(messageLabel, BorderLayout.CENTER);
        card.add(timeLabel, BorderLayout.SOUTH);
        
        return card;
    }

    private void markAllAsRead() {
        String userId = userController.getCurrentUser().getId();
        notificationController.markAllAsRead(userId);
        refreshNotifications();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> refreshNotifications()); // Refresh every 30 seconds
        refreshTimer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
} 