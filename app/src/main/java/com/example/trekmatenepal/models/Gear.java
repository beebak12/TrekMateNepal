package com.example.trekmatenepal.models;

public class Gear {

    private int image;
    private String name;
    private String price;
    private String status;
    private String location;
    private String bookedFrom;
    private String bookedTo;

    public Gear(int image, String name, String price,
                String status, String location,
                String bookedFrom, String bookedTo) {

        this.image = image;
        this.name = name;
        this.price = price;
        this.status = status;
        this.location = location;
        this.bookedFrom = bookedFrom;
        this.bookedTo = bookedTo;
    }

    public Gear(String name, String price, String status, int image) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.image = image;
        this.location = "Unknown";
        this.bookedFrom = "";
        this.bookedTo = "";
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getBookedFrom() {
        return bookedFrom;
    }

    public String getBookedTo() {
        return bookedTo;
    }
}