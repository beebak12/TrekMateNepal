package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * NotificationModel — one in-app notification.
 */
public class NotificationModel implements Serializable {

    private int id;
    private String type;          // "rental" | "listing" | "chat" | "partner"
    private String title;
    private String message;
    private String timeLabel;     // e.g. "10 min ago"
    private boolean isRead;
    private String recipientId;
    private long timestamp;
    private String bookingId;

    public NotificationModel(int id, String type, String title, String message, String timeLabel, boolean isRead) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.message = message;
        this.timeLabel = timeLabel;
        this.isRead = isRead;
    }

    // Extended constructor for backend sync compatibility
    public NotificationModel(String recipientId, String title, String message,
                             String timeLabel, long timestamp,
                             String bookingId, String type, boolean isRead) {
        this.recipientId = recipientId;
        this.title       = title;
        this.message     = message;
        this.timeLabel   = timeLabel;
        this.timestamp   = timestamp;
        this.bookingId   = bookingId;
        this.type        = type;
        this.isRead      = isRead;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type != null ? type : "rental"; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title != null ? title : ""; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message != null ? message : ""; }
    public void setMessage(String message) { this.message = message; }

    public String getTimeLabel() { return timeLabel != null ? timeLabel : ""; }
    public void setTimeLabel(String timeLabel) { this.timeLabel = timeLabel; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getRecipientId() { return recipientId; }
    public long getTimestamp() { return timestamp; }
    public String getBookingId() { return bookingId; }
}