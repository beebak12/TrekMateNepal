package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PostAdapter;
import com.example.trekmatenepal.adapters.TrekAdapter;
import com.example.trekmatenepal.models.PostModel;
import com.example.trekmatenepal.models.TrekModel;


import com.example.trekmatenepal.adapters.GearAdapter;
import com.example.trekmatenepal.models.GearModel;

import com.example.trekmatenepal.adapters.PartnerAdapter;
import com.example.trekmatenepal.models.PartnerModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private CardView cardRentGear;
    private CardView cardPartner;
    private CardView cardPostGear;
    private CardView cardTreks;

    private ImageView profileImage;
    private ImageButton notificationBtn;

    private Button exploreBtn;

    private RecyclerView recyclerTreks;

    private TextView viewAllTreks;

    private RecyclerView recyclerGear;
    private ArrayList<GearModel> gearList;

    private RecyclerView recyclerPartners;
    private ArrayList<PartnerModel> partnerList;

    private RecyclerView recyclerPosts;
    private ArrayList<PostModel> postList;

    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();

        setupRecyclerView();

        setupGearRecyclerView();
        clickListeners();

        setupPartnerRecyclerView();

        clickListeners();

        setupPostRecyclerView();

        setupBottomNavigation();

    }

    private void initializeViews() {

        cardRentGear = findViewById(R.id.cardRentGear);
        cardPartner = findViewById(R.id.cardPartner);
        cardPostGear = findViewById(R.id.cardPostGear);
        cardTreks = findViewById(R.id.cardTreks);

        profileImage = findViewById(R.id.profileImage);
        notificationBtn = findViewById(R.id.notificationBtn);

        exploreBtn = findViewById(R.id.exploreBtn);

        recyclerTreks = findViewById(R.id.recyclerTreks);

        recyclerGear = findViewById(R.id.recyclerGear);


        viewAllTreks = findViewById(R.id.viewAllTreks);

        recyclerPartners = findViewById(R.id.recyclerPartners);


        recyclerPosts = findViewById(R.id.recyclerPosts);

        bottomNavigation = findViewById(R.id.bottomNavigation);

    }

    private void setupRecyclerView() {

        ArrayList<TrekModel> trekList = new ArrayList<>();

        trekList.add(new TrekModel(
                "Everest Base Camp",
                "Khumbu, Nepal",
                "14 Days",
                R.drawable.everest,
                "4.9",
                287,
                "Difficult",
                "5,364m",
                "130 km",
                "The ultimate Himalayan adventure — stand at the foot of the world's highest peak. Trek through the legendary Khumbu Valley, visiting ancient monasteries and Sherpa villages along the route.",
                "Rs. 25,000"));

        trekList.add(new TrekModel(
                "Annapurna Circuit",
                "Manang/Mustang, Nepal",
                "12 Days",
                R.drawable.annapurna,
                "4.7",
                198,
                "Moderate",
                "5,416m",
                "160 km",
                "One of the most diverse treks in the world, crossing Thorong La Pass and descending into the mystical Mustang region.",
                "Rs. 22,000"));

        trekList.add(new TrekModel(
                "Langtang Valley",
                "Rasuwa, Nepal",
                "7 Days",
                R.drawable.langtang,
                "4.6",
                80,
                "Easy",
                "3,800m",
                "77 km",
                "A beautiful trek close to Kathmandu, offering stunning mountain views and insight into Tamang culture.",
                "Rs. 15,000"));

        trekList.add(new TrekModel(
                "Manaslu Circuit",
                "Gorkha, Nepal",
                "14 Days",
                R.drawable.mardihimal,
                "4.9",
                60,
                "Difficult",
                "5,106m",
                "177 km",
                "A restricted area trek offering a true wilderness experience around the world's eighth highest mountain.",
                "Rs. 28,000"));

        TrekAdapter adapter = new TrekAdapter(trekList);

        recyclerTreks.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false));

        recyclerTreks.setAdapter(adapter);

    }

    private void setupGearRecyclerView() {

        gearList = new ArrayList<>();

        gearList.add(new GearModel(
                "Down Jacket",
                "Rs. 2,000",
                "/ week",
                R.drawable.jacket));

        gearList.add(new GearModel(
                "Sleeping Bag",
                "Rs. 1,000",
                "/ week",
                R.drawable.sleepingbag));

        gearList.add(new GearModel(
                "Trekking Boots",
                "Rs. 1,500",
                "/ week",
                R.drawable.boots));

        gearList.add(new GearModel(
                "Backpack 60L",
                "Rs. 1,800",
                "/ week",
                R.drawable.backpack));

        gearList.add(new GearModel(
                "Trekking Pole",
                "Rs. 300",
                "/ week",
                R.drawable.poles));

        GearAdapter adapter = new GearAdapter(gearList);

        recyclerGear.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false));

        recyclerGear.setAdapter(adapter);
    }

    private void clickListeners() {

        cardRentGear.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        GearRentalActivity.class)));

        cardPartner.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        PartnerFinderActivity.class)));

        cardPostGear.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        PostGearActivity.class)));

        cardTreks.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        TrekPackageActivity.class)));

        notificationBtn.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        NotificationActivity.class)));

        profileImage.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        ProfileActivity.class)));

        exploreBtn.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        TrekListActivity.class)));

        viewAllTreks.setOnClickListener(v ->
                startActivity(new Intent(
                        DashboardActivity.this,
                        TrekListActivity.class)));

    }

    private void setupPartnerRecyclerView() {

        partnerList = new ArrayList<>();

        partnerList.add(new PartnerModel(
                "Nirajan Tamang",
                "5.0",
                "(32)",
                "Available",
                R.drawable.partner1));

        partnerList.add(new PartnerModel(
                "Pema Sherpa",
                "4.9",
                "(28)",
                "Available",
                R.drawable.partner2));

        partnerList.add(new PartnerModel(
                "Ramesh Gurung",
                "4.8",
                "(21)",
                "Available",
                R.drawable.partner3));

        partnerList.add(new PartnerModel(
                "Dawa Lama",
                "4.9",
                "(19)",
                "Available",
                R.drawable.partner4));

        PartnerAdapter adapter = new PartnerAdapter(partnerList);

        recyclerPartners.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false));

        recyclerPartners.setAdapter(adapter);
    }

    private void setupPostRecyclerView(){

        postList=new ArrayList<>();

        postList.add(new PostModel(
                "Best time for Annapurna Circuit?",
                "Sandeep Magar",
                "2h ago",
                "24",
                "12",
                R.drawable.annapurna));

        postList.add(new PostModel(
                "Need trekking partner for Everest Base Camp",
                "Aayush Rai",
                "5h ago",
                "18",
                "8",
                R.drawable.everest));

        postList.add(new PostModel(
                "Selling almost new trekking boots (Size 42)",
                "Bikram Thapa",
                "1d ago",
                "15",
                "6",
                R.drawable.boots));

        postList.add(new PostModel(
                "Tips for first time trekkers",
                "Nima Sherpa",
                "2d ago",
                "30",
                "14",
                R.drawable.banner));

        PostAdapter adapter=new PostAdapter(postList);

        recyclerPosts.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerPosts.setAdapter(adapter);

    }
    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_gear) {
                startActivity(new Intent(this, GearRentalActivity.class));
                return true;
            } else if (id == R.id.nav_partner) {
                startActivity(new Intent(this, PartnerFinderActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

}