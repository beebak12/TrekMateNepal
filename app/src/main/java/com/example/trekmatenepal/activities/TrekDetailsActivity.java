package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PartnerAdapter;
import com.example.trekmatenepal.models.PartnerModel;

import java.util.ArrayList;
import java.util.List;

public class TrekDetailsActivity extends AppCompatActivity {

    private ImageView imgTrekDetail, btnBack;
    private TextView txtTrekNameDetail, txtRatingDetail, txtDurationDetail, txtDescriptionDetail, txtRouteDetail, txtEquipmentDetail;
    private RecyclerView recyclerGuides;
    private Button btnBookPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trek_details);

        initializeViews();
        getDataFromIntent();
        setupGuidesRecycler();

        btnBack.setOnClickListener(v -> finish());
        btnBookPackage.setOnClickListener(v -> {
            // Take to booking screen (reuse Gear Booking screen or create new)
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("name", txtTrekNameDetail.getText().toString());
            intent.putExtra("price", "Rs. 25,000"); // Sample price
            intent.putExtra("image", getIntent().getIntExtra("image", R.drawable.everest));
            startActivity(intent);
        });
    }

    private void initializeViews() {
        imgTrekDetail = findViewById(R.id.imgTrekDetail);
        btnBack = findViewById(R.id.btnBack);
        txtTrekNameDetail = findViewById(R.id.txtTrekNameDetail);
        txtRatingDetail = findViewById(R.id.txtRatingDetail);
        txtDurationDetail = findViewById(R.id.txtDurationDetail);
        txtDescriptionDetail = findViewById(R.id.txtDescriptionDetail);
        txtRouteDetail = findViewById(R.id.txtRouteDetail);
        txtEquipmentDetail = findViewById(R.id.txtEquipmentDetail);
        recyclerGuides = findViewById(R.id.recyclerGuides);
        btnBookPackage = findViewById(R.id.btnBookPackage);
    }

    private void getDataFromIntent() {
        String name = getIntent().getStringExtra("name");
        String duration = getIntent().getStringExtra("duration");
        String rating = getIntent().getStringExtra("rating");
        int reviews = getIntent().getIntExtra("reviews", 120);
        int image = getIntent().getIntExtra("image", R.drawable.everest);

        txtTrekNameDetail.setText(name);
        txtDurationDetail.setText(duration);
        txtRatingDetail.setText(rating + " (" + reviews + " reviews)");
        imgTrekDetail.setImageResource(image);

        // Customize description and route based on trek name if needed
        if (name != null && name.contains("Annapurna")) {
            txtDescriptionDetail.setText("The Annapurna Circuit is a trek within the mountain ranges of central Nepal. The total length of the route varies between 160–230 km.");
            txtRouteDetail.setText("Besisahar - Chamje - Dharapani - Chame - Pisang - Manang - Yak Kharka - Thorong Phedi - Thorong La Pass - Muktinath - Jomsom - Pokhara");
        }
    }

    private void setupGuidesRecycler() {
        List<PartnerModel> guideList = new ArrayList<>();
        guideList.add(new PartnerModel("Lhakpa Sherpa", "5.0", "(45)", "Available", R.drawable.partner1));
        guideList.add(new PartnerModel("Mingma Tamang", "4.9", "(38)", "Available", R.drawable.profile_photo));
        guideList.add(new PartnerModel("Pasang Rai", "4.8", "(29)", "Available", R.drawable.partner3));

        PartnerAdapter adapter = new PartnerAdapter(guideList);
        recyclerGuides.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerGuides.setAdapter(adapter);
    }
}