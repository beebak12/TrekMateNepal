package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.RentalGearAdapter;
import com.example.trekmatenepal.data.GearBackendRepository;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

public class SellerProfileActivity extends AppCompatActivity {
    public static final String EXTRA_SELLER_ID = "seller_id";
    public static final String EXTRA_SELLER_NAME = "seller_name";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        String sellerId = value(getIntent().getStringExtra(EXTRA_SELLER_ID));
        String sellerName = value(getIntent().getStringExtra(EXTRA_SELLER_NAME));
        if (sellerName.isEmpty()) sellerName = sellerId.isEmpty() ? "Seller" : sellerId;

        ((TextView) findViewById(R.id.tvSellerName)).setText(sellerName);
        TextView empty = findViewById(R.id.tvEmptySellerGear);
        RecyclerView recycler = findViewById(R.id.recyclerSellerGear);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        ArrayList<RentalGearModel> listings = new ArrayList<>();
        recycler.setAdapter(new RentalGearAdapter(this, listings));
        final String expectedId = sellerId;
        final String expectedName = sellerName;
        GearBackendRepository.getAll(new GearBackendRepository.ListCallback() {
            @Override public void success(ArrayList<RentalGearModel> gearItems) {
                String location = "Nepal";
                for (RentalGearModel gear : gearItems) {
                    boolean same = !expectedId.isEmpty() && expectedId.equalsIgnoreCase(gear.getSellerId());
                    if (!same && expectedId.isEmpty()) same = expectedName.equalsIgnoreCase(gear.getSeller());
                    if (same) { listings.add(gear); if (!gear.getLocation().isEmpty()) location = gear.getLocation(); }
                }
                recycler.getAdapter().notifyDataSetChanged();
                ((TextView) findViewById(R.id.tvSellerLocation)).setText(location);
                ((TextView) findViewById(R.id.tvSellerListingCount)).setText(listings.size() + " gear listings");
                empty.setVisibility(listings.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void error(String message) { empty.setText(message); empty.setVisibility(View.VISIBLE); }
        });
    }

    private String value(String input) { return input == null ? "" : input.trim(); }
}
