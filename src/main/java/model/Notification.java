package model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private boolean read;
    private LocalDateTime timestamp;

    public Notification(String id, String userId, String message, NotificationType type) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.read = false;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public NotificationType getType() { return type; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getTimestamp() { return timestamp; }
} 