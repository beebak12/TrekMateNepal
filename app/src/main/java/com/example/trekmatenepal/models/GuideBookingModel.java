package com.example.trekmatenepal.models;

public class GuideBookingModel {
    private String trekkerName;
    private String destination;
    private String dates;
    private String groupSize;
    private String price;
    private String status;
    private int trekkerImage;

    public GuideBookingModel(String trekkerName, String destination, String dates, String groupSize, String price, String status, int trekkerImage) {
        this.trekkerName = trekkerName;
        this.destination = destination;
        this.dates = dates;
        this.groupSize = groupSize;
        this.price = price;
        this.status = status;
        this.trekkerImage = trekkerImage;
    }

    public String getTrekkerName() { return trekkerName; }
    public String getDestination() { return destination; }
    public String getDates() { return dates; }
    public String getGroupSize() { return groupSize; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }
    public int getTrekkerImage() { return trekkerImage; }

    public void setStatus(String status) { this.status = status; }
}