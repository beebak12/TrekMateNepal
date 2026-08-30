package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.RentalGearAdapter;
import com.example.trekmatenepal.data.GearRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.RentalGearModel;

import java.util.ArrayList;

public class MyPostedGearsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);

        setupHeader();
        initViews();
        loadMyPostedGears();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        TextView title = header.findViewById(R.id.txtHeaderTitle);
        if (title != null) title.setText("My Posted Gears");
        View btnBack = header.findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        layoutEmpty = findViewById(R.id.tvEmpty);
    }

    private void loadMyPostedGears() {
        String currentUserId = SessionUser.getUserId(this);
        ArrayList<RentalGearModel> allGear = GearRepository.getUserGear(this);
        ArrayList<RentalGearModel> myGear = new ArrayList<>();

        for (RentalGearModel g : allGear) {
            if (currentUserId.equalsIgnoreCase(g.getSellerId())) {
                myGear.add(g);
            }
        }

        if (myGear.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            RentalGearAdapter adapter = new RentalGearAdapter(this, myGear);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(adapter);
        }
    }
}
