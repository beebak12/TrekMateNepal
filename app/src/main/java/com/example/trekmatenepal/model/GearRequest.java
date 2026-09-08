package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class GearRequest {
    private final String category, name, description, availability, size, location;
    @SerializedName("price_per_day") private final double price;
    private final int quantity = 1;
    @SerializedName("condition_status") private final String condition;
    public GearRequest(String category, String name, String description, double price,
                       String availability, String condition, String size, String location) {
        this.category = category; this.name = name; this.description = description;
        this.price = price; this.availability = availability;
        this.condition = condition; this.size = size; this.location = location;
    }
}
