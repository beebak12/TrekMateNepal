package com.example.trekmatenepal.model;

public class PartnerPostRequest {
    private final Integer trek_id;
    private final int required_partners;
    private final String travel_date;
    private final String expected_duration;
    private final String experience_level;
    private final String gender_preference;
    private final String group_name;
    private final String description;

    public PartnerPostRequest(Integer trekId, int requiredPartners, String travelDate,
                              String expectedDuration, String experienceLevel,
                              String genderPreference, String groupName, String description) {
        this.trek_id = trekId;
        this.required_partners = requiredPartners;
        this.travel_date = travelDate;
        this.expected_duration = expectedDuration;
        this.experience_level = experienceLevel;
        this.gender_preference = genderPreference;
        this.group_name = groupName;
        this.description = description;
    }
}
