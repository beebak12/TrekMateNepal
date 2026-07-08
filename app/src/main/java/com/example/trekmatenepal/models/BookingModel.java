package com.example.trekmatenepal.models;

public class BookingModel {
    private String gearName;
    private String dates;
    private String amount;
    private String bookingId;
    private String status;
    private int image;

    public BookingModel(String gearName, String dates, String amount, String bookingId, String status, int image) {
        this.gearName = gearName;
        this.dates = dates;
        this.amount = amount;
        this.bookingId = bookingId;
        this.status = status;
        this.image = image;
    }

    public String getGearName() {
        return gearName;
    }

    public String getDates() {
        return dates;
    }

    public String getAmount() {
        return amount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getStatus() {
        return status;
    }

    public int getImage() {
        return image;
    }
}