package com.example.trekmatenepal.models;

import java.io.Serializable;

/**
 * BookingModel holds all data for a gear rental booking.
 * Designed to be passed between Activities via Intent extras (Serializable).
 * Mock/local data for now — replace with API calls when backend is ready.
 */
public class BookingModel implements Serializable {

    private String gearName;
    private String dates;          // Display string e.g. "20 Aug - 27 Aug 2026"
    private String startDate;      // e.g. "20 Aug 2026"
    private String endDate;        // e.g. "27 Aug 2026"
    private String amount;         // Total amount string e.g. "Rs. 8,000"
    private String bookingId;
    private String status;         // Confirmed / Completed / Cancelled
    private int image;
    private int quantity;
    private String pickupLocation;
    private String notes;
    private String pricePerWeek;   // Raw price string e.g. "2000"
    private int durationDays;      // Number of rental days
    private String renterName;
    private String renterPhone;
    private String bookedDate;

    // ── Full constructor (used when creating a new booking) ──────────────────
    public BookingModel(String gearName, String startDate, String endDate,
                        String dates, int quantity, String pickupLocation,
                        String notes, String pricePerWeek, int durationDays,
                        String amount, String bookingId, String status, int image) {
        this.gearName = gearName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dates = dates;
        this.quantity = quantity;
        this.pickupLocation = pickupLocation;
        this.notes = notes;
        this.pricePerWeek = pricePerWeek;
        this.durationDays = durationDays;
        this.amount = amount;
        this.bookingId = bookingId;
        this.status = status;
        this.image = image;
        this.renterName = "";
        this.renterPhone = "";
        this.bookedDate = "";
    }

    /** Constructor including renter details. */
    public BookingModel(String gearName, String startDate, String endDate,
                        String dates, int quantity, String pickupLocation,
                        String notes, String pricePerWeek, int durationDays,
                        String amount, String bookingId, String status, int image,
                        String renterName, String renterPhone, String bookedDate) {
        this.gearName = gearName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dates = dates;
        this.quantity = quantity;
        this.pickupLocation = pickupLocation;
        this.notes = notes;
        this.pricePerWeek = pricePerWeek;
        this.durationDays = durationDays;
        this.amount = amount;
        this.bookingId = bookingId;
        this.status = status;
        this.image = image;
        this.renterName = renterName;
        this.renterPhone = renterPhone;
        this.bookedDate = bookedDate;
    }

    // ── Legacy constructor used by existing MyBookingsActivity sample data ───
    public BookingModel(String gearName, String dates, String amount,
                        String bookingId, String status, int image) {
        this.gearName = gearName;
        this.dates = dates;
        this.amount = amount;
        this.bookingId = bookingId;
        this.status = status;
        this.image = image;
        this.quantity = 1;
        this.pickupLocation = "Kathmandu";
        this.notes = "";
        this.pricePerWeek = "";
        this.durationDays = 0;
        this.startDate = "";
        this.endDate = "";
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getGearName()       { return gearName != null ? gearName : ""; }
    public String getDates()          { return dates != null ? dates : ""; }
    public String getStartDate()      { return startDate != null ? startDate : ""; }
    public String getEndDate()        { return endDate != null ? endDate : ""; }
    public String getAmount()         { return amount != null ? amount : ""; }
    public String getBookingId()      { return bookingId != null ? bookingId : ""; }
    public String getStatus()         { return status != null ? status : ""; }
    public int    getImage()          { return image; }
    public int    getQuantity()       { return quantity; }
    public String getPickupLocation() { return pickupLocation != null ? pickupLocation : ""; }
    public String getNotes()          { return notes != null ? notes : ""; }
    public String getPricePerWeek()   { return pricePerWeek != null ? pricePerWeek : ""; }
    public int    getDurationDays()   { return durationDays; }
    public String getRenterName()     { return renterName != null ? renterName : ""; }
    public String getRenterPhone()    { return renterPhone != null ? renterPhone : ""; }
    public String getBookedDate()     { return bookedDate != null ? bookedDate : ""; }

    // ── Setters (for status update e.g. cancel) ──────────────────────────────
    public void setStatus(String status) { this.status = status; }

    // ── Price calculation helper ─────────────────────────────────────────────
    /**
     * Calculate total rental cost.
     * @param pricePerWeekInt  price in Rs per week
     * @param qty              number of items
     * @param days             total rental days
     * @return                 total in Rs
     */
    public static int calculateTotal(int pricePerWeekInt, int qty, int days) {
        // Convert days → weeks (round up)
        int weeks = (int) Math.ceil(days / 7.0);
        if (weeks < 1) weeks = 1;
        return pricePerWeekInt * qty * weeks;
    }
}
