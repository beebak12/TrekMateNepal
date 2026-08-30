package com.example.trekmatenepal.models;

import java.io.Serializable;
import java.util.UUID;

public class PostModel implements Serializable {

    private String id;
    private String title;
    private String author;
    private String authorId;
    private String location;
    private String dateRange;
    private String duration;
    private String interestedCount;
    private int imageRes;
    private String customImageUri;
    private String description;
    private String budget;
    private String experienceLevel;
    private String groupId;

    public PostModel(String title, String author, String location, String dateRange, String duration, String interestedCount, int imageRes) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.location = location;
        this.dateRange = dateRange;
        this.duration = duration;
        this.interestedCount = interestedCount;
        this.imageRes = imageRes;
        this.groupId = "group_" + this.id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getLocation() { return location; }
    public String getDateRange() { return dateRange; }
    public String getDuration() { return duration; }
    public String getInterestedCount() { return interestedCount; }
    public void setInterestedCount(String count) { this.interestedCount = count; }

    public int getImageRes() { return imageRes; }
    
    public String getCustomImageUri() { return customImageUri; }
    public void setCustomImageUri(String uri) { this.customImageUri = uri; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String level) { this.experienceLevel = level; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
}
