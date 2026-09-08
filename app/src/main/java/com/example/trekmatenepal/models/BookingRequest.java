package com.example.trekmatenepal.models;

public class BookingRequest {

    private String trekkerName;
    private String trekName;
    private String trekkerImage;
    private String bookingDate;
    private String numberOfTrekkers;
    private String price;

    public BookingRequest(String trekkerName,
                          String trekName,
                          String trekkerImage,
                          String bookingDate,
                          String numberOfTrekkers,
                          String price) {

        this.trekkerName = trekkerName;
        this.trekName = trekName;
        this.trekkerImage = trekkerImage;
        this.bookingDate = bookingDate;
        this.numberOfTrekkers = numberOfTrekkers;
        this.price = price;
    }

    public String getTrekkerName() {
        return trekkerName;
    }

    public String getTrekName() {
        return trekName;
    }

    public String getTrekkerImage() {
        return trekkerImage;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getNumberOfTrekkers() {
        return numberOfTrekkers;
    }

    public String getPrice() {
        return price;
    }
}