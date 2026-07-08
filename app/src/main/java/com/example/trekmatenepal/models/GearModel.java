package com.example.trekmatenepal.models;

public class GearModel {

    private String name;
    private String price;
    private String duration;
    private int image;

    public GearModel(String name, String price, String duration, int image) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public int getImage() {
        return image;
    }
}