package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * RentalGearModel — represents a piece of trekking gear available for rental.
 */
public class RentalGearModel implements Serializable {

    private int image;             // Local drawable resource ID
    private String customImageUri; // URI string for user-picked images
    private String name;
    private String category;
    private String rating;
    private String price;          // Display price e.g. "Rs. 2,000 / week"
    private String priceRaw;       // Raw numeric price string
    private String availability;   // "Available" or "Unavailable"
    private String location;
    private String description;
    private String size;
    private String condition;
    private String seller;
    private String sellerId;

    // ── Old short constructor ───────────────────────────────────────────────
    public RentalGearModel(int image, String name, String category,
                           String rating, String price, String availability) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.availability = availability;
        this.priceRaw = extractNumericPrice(price);
        this.location = "Kathmandu";
        this.description = name + " — high quality trekking gear.";
        this.size = "One Size";
        this.condition = "Excellent";
        this.seller = "Trek Gear Nepal";
    }

    // ── Custom Route Constructor (Legacy-ish but needed for GearRentalActivity) ──
    public RentalGearModel(int image, String name, String category, String rating,
                           String price, String availability, String location,
                           String description) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.availability = availability;
        this.location = location;
        this.description = description;
        this.priceRaw = extractNumericPrice(price);
        this.size = "One Size";
        this.condition = "Excellent";
        this.seller = "Trek Gear Nepal";
    }

    // ── Legacy full constructor for GearRentalActivity seed data ───────────
    public RentalGearModel(int image, String name, String category, String rating,
                           String price, String priceRaw, String availability,
                           String location, String description,
                           String size, String condition, String seller) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.priceRaw = priceRaw;
        this.availability = availability;
        this.location = location;
        this.description = description;
        this.size = size;
        this.condition = condition;
        this.seller = seller;
    }

    // ── Full constructor ──────────────────────────────────────────────────
    public RentalGearModel(int image, String customImageUri, String name, String category, String rating,
                           String price, String priceRaw, String availability,
                           String location, String description,
                           String size, String condition, String seller, String sellerId) {
        this.image = image;
        this.customImageUri = customImageUri;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.priceRaw = priceRaw;
        this.availability = availability;
        this.location = location;
        this.description = description;
        this.size = size;
        this.condition = condition;
        this.seller = seller;
        this.sellerId = sellerId;
    }

    private String extractNumericPrice(String priceStr) {
        if (priceStr == null) return "0";
        String cleaned = priceStr.replaceAll("Rs\\.\\s*", "").replaceAll(",", "").trim();
        int slashIdx = cleaned.indexOf('/');
        if (slashIdx > 0) cleaned = cleaned.substring(0, slashIdx).trim();
        return cleaned;
    }

    // ── Getters & Setters ───────────────────────────────────────────────────
    public int    getImage()           { return image; }
    public String getCustomImageUri()  { return customImageUri; }
    public void   setCustomImageUri(String uri) { this.customImageUri = uri; }
    public String getName()            { return name != null ? name : ""; }
    public String getCategory()        { return category != null ? category : ""; }
    public String getRating()          { return rating != null ? rating : ""; }
    public String getPrice()           { return price != null ? price : ""; }
    public String getPriceRaw()        { return priceRaw != null ? priceRaw : "0"; }
    public String getAvailability()    { return availability != null ? availability : ""; }
    public String getLocation()        { return location != null ? location : ""; }
    public String getDescription()     { return description != null ? description : ""; }
    public String getSize()            { return size != null ? size : ""; }
    public String getCondition()       { return condition != null ? condition : ""; }
    public String getSeller()          { return seller != null ? seller : ""; }
    public String getSellerId()        { return sellerId != null ? sellerId : ""; }
}
