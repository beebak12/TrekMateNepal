package com.example.trekmatenepal.models;

public class TrekModel {
    private String name;
    private String location;
    private String duration;
    private int image;
    private String rating;
    private int reviews;
    private String difficulty;
    private String maxAltitude;
    private String distance;
    private String description;
    private String price;

    public TrekModel(String name, String location, String duration, int image, String rating, int reviews, String difficulty, String maxAltitude, String distance, String description, String price) {
        this.name = name;
        this.location = location;
        this.duration = duration;
        this.image = image;
        this.rating = rating;
        this.reviews = reviews;
        this.difficulty = difficulty;
        this.maxAltitude = maxAltitude;
        this.distance = distance;
        this.description = description;
        this.price = price;
    }

    public TrekModel(int image, String name, String location, String duration, String rating) {
        this.image = image;
        this.name = name;
        this.location = location;
        this.duration = duration;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDuration() { return duration; }
    public int getImage() { return image; }
    public String getRating() { return rating; }
    public int getReviews() { return reviews; }
    public String getDifficulty() { return difficulty; }
    public String getMaxAltitude() { return maxAltitude; }
    public String getDistance() { return distance; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
}
