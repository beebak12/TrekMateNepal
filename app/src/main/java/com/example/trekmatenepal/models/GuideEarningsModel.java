package com.example.trekmatenepal.models;

public class GuideEarningsModel {
    private String trekName;
    private String dates;
    private String amount;
    private String status;

    public GuideEarningsModel(String trekName, String dates, String amount, String status) {
        this.trekName = trekName;
        this.dates = dates;
        this.amount = amount;
        this.status = status;
    }

    public String getTrekName() { return trekName; }
    public String getDates() { return dates; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }
}