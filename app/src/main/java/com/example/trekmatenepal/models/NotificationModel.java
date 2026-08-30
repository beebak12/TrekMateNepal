package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * NotificationModel — one in-app notification addressed to a user / seller id.
 */
public class NotificationModel implements Serializable {

    private final String recipientId;   // user id or seller id this is addressed to
    private final String title;
    private final String message;
    private final String timeLabel;     // e.g. "27 Aug 2026, 04:12 PM"
    private final long   timestamp;
    private final String bookingId;     // may be empty
    private final String type;          // "rental" | "listing"

    public NotificationModel(String recipientId, String title, String message,
                             String timeLabel, long timestamp,
                             String bookingId, String type) {
        this.recipientId = recipientId;
        this.title       = title;
        this.message     = message;
        this.timeLabel   = timeLabel;
        this.timestamp   = timestamp;
        this.bookingId   = bookingId;
        this.type        = type;
    }

    public String getRecipientId() { return recipientId != null ? recipientId : ""; }
    public String getTitle()       { return title != null ? title : ""; }
    public String getMessage()     { return message != null ? message : ""; }
    public String getTimeLabel()   { return timeLabel != null ? timeLabel : ""; }
    public long   getTimestamp()   { return timestamp; }
    public String getBookingId()   { return bookingId != null ? bookingId : ""; }
    public String getType()        { return type != null ? type : "rental"; }
}
