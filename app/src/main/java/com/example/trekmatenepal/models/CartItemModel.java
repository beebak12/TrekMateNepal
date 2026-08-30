package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * CartItemModel — one gear line inside a booking.
 *
 * A booking can hold several of these, so the user can keep adding items
 * from the summary screen before confirming.
 */
public class CartItemModel implements Serializable {

    private final int    image;
    private final String gearName;
    private final String seller;        // seller / owner id that gets notified
    private final String gearCity;      // city the gear is available in
    private final int    pricePerWeek;  // Rs per week, per item
    private final int    quantity;
    private final int    days;
    private final String dates;         // display string "20 Aug 2026 → 27 Aug 2026"
    private final String pickupLocation;
    private final String notes;

    public CartItemModel(int image, String gearName, String seller, String gearCity,
                         int pricePerWeek, int quantity, int days, String dates,
                         String pickupLocation, String notes) {
        this.image          = image;
        this.gearName       = gearName;
        this.seller         = seller;
        this.gearCity       = gearCity;
        this.pricePerWeek   = pricePerWeek;
        this.quantity       = Math.max(1, quantity);
        this.days           = Math.max(1, days);
        this.dates          = dates;
        this.pickupLocation = pickupLocation;
        this.notes          = notes;
    }

    public int    getImage()          { return image; }
    public String getGearName()       { return gearName != null ? gearName : ""; }
    public String getSeller()         { return seller != null && !seller.trim().isEmpty() ? seller : "Trek Gear Nepal"; }
    public String getGearCity()       { return gearCity != null ? gearCity.trim() : ""; }
    public int    getPricePerWeek()   { return pricePerWeek; }
    public int    getQuantity()       { return quantity; }
    public int    getDays()           { return days; }
    public String getDates()          { return dates != null ? dates : ""; }
    public String getPickupLocation() { return pickupLocation != null ? pickupLocation : ""; }
    public String getNotes()          { return notes != null ? notes : ""; }

    /** Rental weeks, rounded up (matches BookingModel.calculateTotal). */
    public int getWeeks() {
        int weeks = (int) Math.ceil(days / 7.0);
        return Math.max(1, weeks);
    }

    /** Line total in Rs: price per week × quantity × weeks. */
    public int getSubtotal() {
        return pricePerWeek * quantity * getWeeks();
    }
}
