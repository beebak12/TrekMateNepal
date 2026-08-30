package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * PartnerModel — represents a trekking partner or guide.
 * Serializable so it can be passed between Activities via Intent extras.
 * Replace image ints with URLs when backend is connected.
 */
public class PartnerModel implements Serializable {

    // ── Core fields (used by Dashboard + existing adapters) ──────────────────
    private String name;
    private String rating;
    private String reviews;
    private String status;          // "Available" / "Busy" / "Online"
    private int    image;

    // ── Extended fields for Partner List & Profile ───────────────────────────
    private String location;        // e.g. "Pokhara, Nepal"
    private String destination;     // e.g. "Everest Base Camp"
    private String trekDate;        // e.g. "20 Apr – 2 May"
    private String duration;        // e.g. "12 Days"
    private String age;             // e.g. "28 Years"
    private String treksCount;      // e.g. "8+"
    private String partners;        // e.g. "15"
    private String about;
    private String interests;       // comma-separated, e.g. "Photography,Camping,Nature"
    private String groupSize;       // e.g. "3 – 5 People"
    private int    coverImage;      // hero cover image resource
    private boolean isOnline;

    // Additional fields from Partner class to unify models
    private int yearsOfExperience;
    private int spotsAvailable;
    private double costPerDay;
    private boolean verified;
    private String phone;
    private String email;
    private String baseLocation;

    // ── Legacy constructor ────────────────────────────────────────────────────
    public PartnerModel(String name, String rating, String reviews,
                        String status, int image) {
        this.name     = name;
        this.rating   = rating;
        this.reviews  = reviews;
        this.status   = status;
        this.image    = image;
        // defaults
        this.location    = "Nepal";
        this.baseLocation = "Nepal";
        this.destination = "EBC Trek";
        this.trekDate    = "Apr – May";
        this.duration    = "14 Days";
        this.age         = "28 Years";
        this.treksCount  = "5+";
        this.partners    = "10";
        this.about       = "Adventure enthusiast looking for trekking companions.";
        this.interests   = "Trekking,Nature,Photography";
        this.groupSize   = "2 – 4 People";
        this.coverImage  = image;
        this.isOnline    = status.equalsIgnoreCase("Available");
        this.yearsOfExperience = 5;
        this.spotsAvailable = 4;
        this.costPerDay = 1500.0;
        this.verified = true;
    }

    // ── Full constructor — used by PartnerListActivity ────────────────────────
    public PartnerModel(String name, String rating, String reviews,
                        String status, int image, String location,
                        String destination, String trekDate, String duration,
                        String age, String treks, String partners,
                        String about, String interests, String groupSize,
                        int coverImage, boolean isOnline) {
        this.name        = name;
        this.rating      = rating;
        this.reviews     = reviews;
        this.status      = status;
        this.image       = image;
        this.location    = location;
        this.destination = destination;
        this.trekDate    = trekDate;
        this.duration    = duration;
        this.age         = age;
        this.treksCount  = treks;
        this.partners    = partners;
        this.about       = about;
        this.interests   = interests;
        this.groupSize   = groupSize;
        this.coverImage  = coverImage;
        this.isOnline    = isOnline;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String  getName()        { return name != null ? name : ""; }
    public String  getRating()      { return rating != null ? rating : ""; }
    public String  getReviews()     { return reviews != null ? reviews : ""; }
    public String  getStatus()      { return status != null ? status : ""; }
    public int     getImage()       { return image; }
    public String  getLocation()    { return location != null ? location : ""; }
    public String  getDestination() { return destination != null ? destination : ""; }
    public String  getTrekDate()    { return trekDate != null ? trekDate : ""; }
    public String  getDuration()    { return duration != null ? duration : ""; }
    public String  getAge()         { return age != null ? age : ""; }
    public String  getTreks()       { return treksCount != null ? treksCount : ""; }
    public String  getPartners()    { return partners != null ? partners : ""; }
    public String  getAbout()       { return about != null ? about : ""; }
    public String  getInterests()   { return interests != null ? interests : ""; }
    public String  getGroupSize()   { return groupSize != null ? groupSize : ""; }
    public int     getCoverImage()  { return coverImage != 0 ? coverImage : image; }
    public boolean isOnline()       { return isOnline; }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public int getSpotsAvailable() { return spotsAvailable; }
    public void setSpotsAvailable(int spotsAvailable) { this.spotsAvailable = spotsAvailable; }

    public double getCostPerDay() { return costPerDay; }
    public void setCostPerDay(double costPerDay) { this.costPerDay = costPerDay; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBaseLocation() { return baseLocation != null ? baseLocation : location; }
    public void setBaseLocation(String baseLocation) { this.baseLocation = baseLocation; }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
