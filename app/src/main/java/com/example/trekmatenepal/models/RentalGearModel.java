package com.example.trekmatenepal.models;

public class RentalGearModel {

    private int image;
    private String name;
    private String category;
    private String rating;
    private String price;
    private String availability;
    private String location;
    private String description;

    public RentalGearModel(int image, String name, String category, String rating, String price, String availability) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.availability = availability;
    }

    public RentalGearModel(int image, String name, String category, String rating, String price, String availability, String location, String description) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.availability = availability;
        this.location = location;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}