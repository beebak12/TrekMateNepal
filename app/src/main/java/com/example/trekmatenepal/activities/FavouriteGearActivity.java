package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.RentalGearAdapter;
import com.example.trekmatenepal.data.GearFavouriteRepository;
import com.example.trekmatenepal.data.GearBackendRepository;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

public class FavouriteGearActivity extends AppCompatActivity {
    private final ArrayList<RentalGearModel> favourites = new ArrayList<>();
    private RentalGearAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_gear);
        View header = findViewById(R.id.headerLayout);
        ((TextView) header.findViewById(R.id.txtHeaderTitle)).setText("Favourite Gear");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        emptyView = findViewById(R.id.tvEmptyFavourites);
        RecyclerView recycler = findViewById(R.id.recyclerFavouriteGear);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RentalGearAdapter(this, favourites, gear -> refresh());
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        favourites.clear();
        favourites.addAll(GearFavouriteRepository.getFavourites(this));
        if (adapter != null) adapter.notifyDataSetChanged();
        emptyView.setVisibility(favourites.isEmpty() ? View.VISIBLE : View.GONE);
        GearBackendRepository.getAll(new GearBackendRepository.ListCallback() {
            @Override public void success(ArrayList<RentalGearModel> gear) {
                GearFavouriteRepository.syncFromServer(FavouriteGearActivity.this, gear, () -> runOnUiThread(FavouriteGearActivity.this::refreshLocal));
            }
            @Override public void error(String message) { }
        });
    }

    private void refreshLocal() {
        favourites.clear(); favourites.addAll(GearFavouriteRepository.getFavourites(this));
        if (adapter != null) adapter.notifyDataSetChanged();
        emptyView.setVisibility(favourites.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
