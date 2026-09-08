package com.example.trekmatenepal.models;

public class Package {

    private int image;
    private String name;
    private String location;
    private String duration;
    private String description;
    private String status;

    public Package(int image, String name, String location,
                   String duration, String description, String status) {

        this.image = image;
        this.name = name;
        this.location = location;
        this.duration = duration;
        this.description = description;
        this.status = status;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}