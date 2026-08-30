package com.example.trekmatenepal.models;

import java.io.Serializable;

public class TrekModel implements Serializable {

    private String trekName;
    private String location;
    private String duration;
    private int image;
    private String rating;
    private int reviews;
    private String difficulty;
    private String altitude;
    private String distance;
    private String description;
    private String fee;

    private String status;

    public TrekModel(int image, String trekName, String location, String duration, String status) {
        this.image = image;
        this.trekName = trekName;
        this.location = location;
        this.duration = duration;
        this.status = status;
        this.rating = "0.0";
        this.reviews = 0;
        this.difficulty = "Unknown";
        this.altitude = "Unknown";
        this.distance = "Unknown";
        this.description = "";
        this.fee = "";
    }

    public TrekModel(String trekName, String location, String duration, int image, String rating, int reviews, 
                     String difficulty, String altitude, String distance, String description, String fee) {
        this.trekName = trekName;
        this.location = location;
        this.duration = duration;
        this.image = image;
        this.rating = rating;
        this.reviews = reviews;
        this.difficulty = difficulty;
        this.altitude = altitude;
        this.distance = distance;
        this.description = description;
        this.fee = fee;
    }

    public String getTrekName() {
        return trekName;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public int getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getDistance() {
        return distance;
    }

    public String getDescription() {
        return description;
    }

    public String getFee() {
        return fee;
    }

    public String getStatus() {
        return status;
    }
}