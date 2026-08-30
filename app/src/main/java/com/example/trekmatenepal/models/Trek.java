package com.example.trekmatenepal.models;

public class Trek {

    private int image;
    private String name;
    private String location;
    private String date;
    private String status;

    public Trek(int image, String name, String location,
                String date, String status) {

        this.image = image;
        this.name = name;
        this.location = location;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}