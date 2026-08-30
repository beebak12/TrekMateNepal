package com.example.trekmatenepal.models;

public class GuidePackageModel {
    private String name;
    private String destination;
    private String duration;
    private String difficulty;
    private String price;
    private String status;
    private int image;

    public GuidePackageModel(String name, String destination, String duration, String difficulty, String price, String status, int image) {
        this.name = name;
        this.destination = destination;
        this.duration = duration;
        this.difficulty = difficulty;
        this.price = price;
        this.status = status;
        this.image = image;
    }

    public String getName() { return name; }
    public String getDestination() { return destination; }
    public String getDuration() { return duration; }
    public String getDifficulty() { return difficulty; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }
    public int getImage() { return image; }
}