package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.CartItemAdapter;
import com.example.trekmatenepal.data.BookingCart;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.BookingModel;
import com.example.trekmatenepal.models.CartItemModel;

import java.util.Locale;

/**
 * BookingSummaryActivity — shows every item in the booking before the user confirms.
 *
 * The user can keep adding gear here ("+ Add More Items") or drop a line, and the
 * price breakdown recalculates across all items. On confirm, each gear's seller id
 * and the renter are notified via NotificationRepository.
 *
 * Backend note: on Confirm, replace the local object pass-through with
 * an API POST request. Save the returned server booking ID.
 */
public class BookingSummaryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtItemsHeading, tvQuantity, tvPickup,
            tvCustomerName, tvCustomerContact, tvBookedDate,
            tvPricePerWeek, tvDuration, tvDeliveryFee, tvTotal;
    private Button btnConfirm, btnAddMoreItems;
    private RecyclerView recyclerCartItems;

    private CartItemAdapter cartAdapter;
    private final BookingCart cart = BookingCart.get();

    private BookingModel booking;   // metadata of the first item (id, pickup, dates)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        initializeViews();
        loadData();

        // Nothing to confirm (e.g. every line was removed) — go back to browsing.
        if (cart.isEmpty()) {
            finish();
            return;
        }

        setupCartRecycler();
        populateUI();

        btnBack.setOnClickListener(v -> finish());
        btnAddMoreItems.setOnClickListener(v -> addMoreItems());
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void initializeViews() {
        btnBack           = findViewById(R.id.btnBack);
        txtItemsHeading   = findViewById(R.id.txtItemsHeading);
        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        btnAddMoreItems   = findViewById(R.id.btnAddMoreItems);
        tvQuantity        = findViewById(R.id.tvQuantity);
        tvPickup          = findViewById(R.id.tvPickup);
        tvCustomerName    = findViewById(R.id.tvCustomerName);
        tvCustomerContact = findViewById(R.id.tvCustomerContact);
        tvBookedDate      = findViewById(R.id.tvBookedDate);
        tvPricePerWeek    = findViewById(R.id.tvPricePerWeek);
        tvDuration        = findViewById(R.id.tvDuration);
        tvDeliveryFee     = findViewById(R.id.tvDeliveryFee);
        tvTotal           = findViewById(R.id.tvTotal);
        btnConfirm        = findViewById(R.id.btnConfirm);
    }

    private void loadData() {
        booking = (BookingModel) getIntent().getSerializableExtra("booking");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Returning here means the user is no longer picking an extra item
        // (they either added one — the cart already has it — or backed out).
        cart.setAddingMore(false);
        if (cartAdapter == null) return;
        if (cart.isEmpty()) {
            finish();
            return;
        }
        cartAdapter.notifyDataSetChanged();
        populateUI();
    }

    // ── Items list ───────────────────────────────────────────────────────────
    private void setupCartRecycler() {
        cartAdapter = new CartItemAdapter(cart.getItems(), this::removeItem);
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerCartItems.setAdapter(cartAdapter);
        recyclerCartItems.setNestedScrollingEnabled(false);
    }

    private void removeItem(int position) {
        cart.remove(position);
        if (cart.isEmpty()) {
            finish();
            return;
        }
        cartAdapter.notifyDataSetChanged();
        populateUI();
    }

    /** Send the user back to Gear Rental to pick another item for this booking. */
    private void addMoreItems() {
        cart.setAddingMore(true);
        Toast.makeText(this, "Pick another gear to add to this booking", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GearRentalActivity.class));
    }

    // ── Totals ───────────────────────────────────────────────────────────────
    private void populateUI() {
        int itemCount = cart.size();
        int pieces    = cart.getTotalQuantity();

        txtItemsHeading.setText(itemCount == 1
                ? "Item in this booking"
                : "Items in this booking (" + itemCount + ")");

        tvQuantity.setText(pieces + " piece" + (pieces == 1 ? "" : "s"));
        tvPickup.setText(pickupLocation());
        tvCustomerName.setText(cart.getCustomerName());
        tvCustomerContact.setText(cart.getCustomerContact());
        tvBookedDate.setText(cart.getBookedDate());

        tvPricePerWeek.setText(rs(cart.getSubtotal()));
        tvDuration.setText(itemCount + " gear item" + (itemCount == 1 ? "" : "s")
                + " · " + pieces + " piece" + (pieces == 1 ? "" : "s"));
        tvDeliveryFee.setText(rs(cart.getDeliveryFee()));
        tvTotal.setText(rs(cart.getTotal()));
    }

    private String pickupLocation() {
        if (booking != null && !booking.getPickupLocation().isEmpty()) {
            return booking.getPickupLocation();
        }
        return cart.isEmpty() ? "" : cart.getItems().get(0).getPickupLocation();
    }

    private String rs(int amount) {
        return "Rs. " + String.format(Locale.getDefault(), "%,d", amount);
    }

    // ── Confirm ──────────────────────────────────────────────────────────────
    private void confirmBooking() {
        if (cart.isEmpty()) return;

        CartItemModel first = cart.getItems().get(0);
        String bookingId = booking != null && !booking.getBookingId().isEmpty()
                ? booking.getBookingId()
                : "BK-" + System.currentTimeMillis() % 100000;

        int total = cart.getTotal();

        BookingModel confirmed = new BookingModel(
                cart.getSummaryName(),
                first.getDates(), first.getDates(), first.getDates(),
                cart.getTotalQuantity(),
                pickupLocation(),
                first.getNotes(),
                String.valueOf(first.getPricePerWeek()),
                first.getDays(),
                rs(total),
                bookingId,
                "Pending",
                first.getImage(),
                cart.getCustomerName(),
                cart.getCustomerContact(),
                cart.getBookedDate()
        );

        Intent intent = new Intent(this, PaymentMethodActivity.class);
        intent.putExtra("booking", confirmed);
        startActivity(intent);
    }
}
