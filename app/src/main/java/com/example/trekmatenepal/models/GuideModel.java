package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * GuideModel — represents a professional trekking guide.
 * Serializable for passing between Activities via Intent extras.
 */
public class GuideModel implements Serializable {

    private String guideId;
    private String name;
    private String designation;      // "Licensed Guide" etc
    private String experience;       // "8+ Years"
    private String languages;        // "English, Nepali, Sherpa"
    private String rating;           // "4.9"
    private int reviews;             // 120
    private String dailyPrice;       // "Rs. 2,500"
    private int image;
    private String about;
    private String specializations;  // comma-separated
    private int coverImage;
    private boolean verified;
    private String successRate;      // "98%"
    private int treksCompleted;      // 120

    // ── Full constructor ──────────────────────────────────────────────────────
    public GuideModel(String guideId, String name, String designation,
                      String experience, String languages, String rating,
                      int reviews, String dailyPrice, int image, String about,
                      String specializations, int coverImage, boolean verified,
                      String successRate, int treksCompleted) {
        this.guideId = guideId;
        this.name = name;
        this.designation = designation;
        this.experience = experience;
        this.languages = languages;
        this.rating = rating;
        this.reviews = reviews;
        this.dailyPrice = dailyPrice;
        this.image = image;
        this.about = about;
        this.specializations = specializations;
        this.coverImage = coverImage;
        this.verified = verified;
        this.successRate = successRate;
        this.treksCompleted = treksCompleted;
    }

    // ── Simple constructor (basic info only) ──────────────────────────────────
    public GuideModel(String guideId, String name, String experience,
                      String languages, String rating, int reviews,
                      String dailyPrice, int image) {
        this.guideId = guideId;
        this.name = name;
        this.experience = experience;
        this.languages = languages;
        this.rating = rating;
        this.reviews = reviews;
        this.dailyPrice = dailyPrice;
        this.image = image;
        this.designation = "Licensed Guide";
        this.about = "Professional trekking guide";
        this.specializations = "Trekking,Mountains";
        this.coverImage = image;
        this.verified = true;
        this.successRate = "95%";
        this.treksCompleted = reviews;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String  getGuideId()         { return guideId != null ? guideId : ""; }
    public String  getName()            { return name != null ? name : ""; }
    public String  getDesignation()     { return designation != null ? designation : ""; }
    public String  getExperience()      { return experience != null ? experience : ""; }
    public String  getLanguages()       { return languages != null ? languages : ""; }
    public String  getRating()          { return rating != null ? rating : "0.0"; }
    public int     getReviews()         { return reviews; }
    public String  getDailyPrice()      { return dailyPrice != null ? dailyPrice : ""; }
    public int     getImage()           { return image; }
    public String  getAbout()           { return about != null ? about : ""; }
    public String  getSpecializations() { return specializations != null ? specializations : ""; }
    public int     getCoverImage()      { return coverImage != 0 ? coverImage : image; }
    public boolean isVerified()         { return verified; }
    public String  getSuccessRate()     { return successRate != null ? successRate : ""; }
    public int     getTripsCompleted()  { return treksCompleted; }
}
