package com.example.trekmatenepal.data;

import com.example.trekmatenepal.models.CartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * BookingCart — the gear items in the booking the user is currently building.
 *
 * In-memory only (one booking session); it is cleared once the booking is
 * confirmed or abandoned. Lets the user keep adding items from the summary
 * screen instead of booking one piece of gear at a time.
 */
public final class BookingCart {

    /** Extra charge per gear item that has to be delivered to another city. */
    public static final int DELIVERY_CHARGE = 150;

    private static BookingCart instance;

    private final List<CartItemModel> items = new ArrayList<>();

    /** True while the user is picking an extra item from the summary screen. */
    private boolean addingMore = false;

    // ── Who is booking (collected on the booking form) ───────────────────────
    private String customerName    = "";
    private String customerContact = "";
    private String deliveryCity    = "";
    private String deliveryAddress = "";
    private String bookedDate      = "";   // when the booking was placed

    private BookingCart() { }

    public static synchronized BookingCart get() {
        if (instance == null) instance = new BookingCart();
        return instance;
    }

    // ── Items ────────────────────────────────────────────────────────────────
    /** Starts a brand-new booking with a single item. */
    public void startNew(CartItemModel item) {
        items.clear();
        items.add(item);
    }

    public void add(CartItemModel item) {
        items.add(item);
    }

    public void remove(int position) {
        if (position >= 0 && position < items.size()) items.remove(position);
    }

    public List<CartItemModel> getItems() {
        return items;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
        addingMore      = false;
        customerName    = "";
        customerContact = "";
        deliveryCity    = "";
        deliveryAddress = "";
        bookedDate      = "";
    }

    // ── Customer details ─────────────────────────────────────────────────────
    /** Stored once on the booking form and reused for every item in the cart. */
    public void setCustomer(String name, String contact, String city,
                            String address, String bookedDate) {
        this.customerName    = safe(name);
        this.customerContact = safe(contact);
        this.deliveryCity    = safe(city);
        this.deliveryAddress = safe(address);
        this.bookedDate      = safe(bookedDate);
    }

    public String getCustomerName()    { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public String getDeliveryCity()    { return deliveryCity; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getBookedDate()      { return bookedDate; }

    private static String safe(String s) { return s == null ? "" : s.trim(); }

    // ── Totals ───────────────────────────────────────────────────────────────
    /** Sum of every line total, in Rs. */
    public int getSubtotal() {
        int sum = 0;
        for (CartItemModel item : items) sum += item.getSubtotal();
        return sum;
    }

    /** Rs. 150 per gear item if the gear's city doesn't match the customer's delivery city. */
    public int getDeliveryFee() {
        if (deliveryCity == null || deliveryCity.isEmpty()) return 0;
        int fee = 0;
        for (CartItemModel item : items) {
            String gearCity = item.getGearCity();
            if (!gearCity.isEmpty() && !gearCity.equalsIgnoreCase(deliveryCity)) {
                fee += DELIVERY_CHARGE;
            }
        }
        return fee;
    }

    public int getTotal() {
        return getSubtotal() + getDeliveryFee();
    }

    /** Total pieces of gear across all lines. */
    public int getTotalQuantity() {
        int qty = 0;
        for (CartItemModel item : items) qty += item.getQuantity();
        return qty;
    }

    /** "Down Jacket" or "Down Jacket + 2 more items" for the booking record. */
    public String getSummaryName() {
        if (items.isEmpty()) return "";
        String first = items.get(0).getGearName();
        int extra = items.size() - 1;
        if (extra <= 0) return first;
        return first + " + " + extra + " more item" + (extra == 1 ? "" : "s");
    }

    // ── "Add more items" flow ────────────────────────────────────────────────
    public boolean isAddingMore() {
        return addingMore;
    }

    public void setAddingMore(boolean addingMore) {
        this.addingMore = addingMore;
    }
}
