package com.example.trekmatenepal.models;

public class PartnerModel {

    private String name;
    private String rating;
    private String reviews;
    private String status;
    private int image;

    public PartnerModel(String name, String rating, String reviews, String status, int image) {
        this.name = name;
        this.rating = rating;
        this.reviews = reviews;
        this.status = status;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getReviews() {
        return reviews;
    }

    public String getStatus() {
        return status;
    }

    public int getImage() {
        return image;
    }
}