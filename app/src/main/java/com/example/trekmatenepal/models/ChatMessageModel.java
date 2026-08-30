package com.example.trekmatenepal.models;

import android.net.Uri;

/**
 * ChatMessageModel — a single chat message.
 * type = TYPE_SENT (user) or TYPE_RECEIVED (partner).
 */
public class ChatMessageModel {

    public static final int TYPE_SENT     = 1;
    public static final int TYPE_RECEIVED = 2;

    private String message;
    private String time;
    private int    type;   // TYPE_SENT or TYPE_RECEIVED
    
    private Uri    attachmentUri;
    private String attachmentName;
    private String attachmentType; // "image", "video", "file"

    public ChatMessageModel(String message, String time, int type) {
        this.message = message;
        this.time    = time;
        this.type    = type;
    }
    
    public ChatMessageModel(String message, String time, int type, Uri attachmentUri, String attachmentName, String attachmentType) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.attachmentUri = attachmentUri;
        this.attachmentName = attachmentName;
        this.attachmentType = attachmentType;
    }

    public String getMessage() { return message != null ? message : ""; }
    public String getTime()    { return time != null ? time : ""; }
    public int    getType()    { return type; }
    
    public Uri getAttachmentUri() { return attachmentUri; }
    public String getAttachmentName() { return attachmentName; }
    public String getAttachmentType() { return attachmentType; }
    public boolean hasAttachment() { return attachmentUri != null; }
}
