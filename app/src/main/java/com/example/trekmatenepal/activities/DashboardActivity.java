package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.PostRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.fragments.ChatBottomSheetFragment;
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

    private CardView cardRentGear, cardPartner, cardPostGear, cardTreks;
    private ImageView profileImage;
    private ImageButton notificationBtn;
    private Button exploreBtn;
    private RecyclerView recyclerTreks, recyclerGear, recyclerPartners, recyclerPosts;
    private TextView viewAllTreks, viewAllGear;
    private TextView fabBadge;
    private BottomNavigationView bottomNavigation;
    private DrawerLayout drawerLayout;

    private ArrayList<GearModel> gearList;
    private ArrayList<PartnerModel> partnerList;
    private ArrayList<PostModel> postList;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        PostRepository.loadPosts(this);
        initializeViews();
        setupRecyclerView();
        setupGearRecyclerView();
        setupPartnerRecyclerView();
        setupPostRecyclerView();
        setupBottomNavigation();
        setupUserMenu();
        setupBackHandling();
        clickListeners();
    }

    private void updateChatBadge() {
        if (fabBadge == null) return;
        int count = com.example.trekmatenepal.data.ChatRepository.getTotalUnreadCount();
        if (count > 0) {
            fabBadge.setVisibility(View.VISIBLE);
            fabBadge.setText(String.valueOf(count));
        } else {
            fabBadge.setVisibility(View.GONE);
        }
    }

    private void setupBackHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.example.trekmatenepal.data.ChatRepository.loadChats(this);
        updateChatBadge();
        
        if (postAdapter != null) {
            postList.clear();
            postList.addAll(PostRepository.getAllPosts());
            postAdapter.notifyDataSetChanged();
        }

        // Handle opening the menu drawer or chat sheet via intent extra
        if (getIntent().getBooleanExtra("open_menu", false)) {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.END);
                getIntent().removeExtra("open_menu");
            }
        } else if (getIntent().getBooleanExtra("open_chat", false)) {
            ChatBottomSheetFragment chatBottomSheet = new ChatBottomSheetFragment();
            chatBottomSheet.show(getSupportFragmentManager(), chatBottomSheet.getTag());
            getIntent().removeExtra("open_chat");
        }
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
        recyclerPartners = findViewById(R.id.recyclerPartners);
        recyclerPosts = findViewById(R.id.recyclerPosts);

        viewAllTreks = findViewById(R.id.viewAllTreks);
        viewAllGear = findViewById(R.id.viewAllGear);
        fabBadge = findViewById(R.id.fabBadge);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        drawerLayout = findViewById(R.id.drawerLayout);
    }

    private void setupRecyclerView() {
        ArrayList<TrekModel> trekList = new ArrayList<>();
        trekList.add(new TrekModel("Everest Base Camp", "Khumbu, Nepal", "14 Days", R.drawable.everest, "4.9", 287, "Difficult", "5,364m", "130 km", "The ultimate Himalayan adventure.", "Rs. 25,000"));
        trekList.add(new TrekModel("Annapurna Circuit", "Manang, Nepal", "12 Days", R.drawable.annapurna, "4.7", 198, "Moderate", "5,416m", "160 km", "One of the most diverse treks.", "Rs. 22,000"));
        trekList.add(new TrekModel("Langtang Valley", "Rasuwa, Nepal", "7 Days", R.drawable.langtang, "4.6", 80, "Easy", "3,800m", "77 km", "Beautiful trek close to Kathmandu.", "Rs. 15,000"));
        trekList.add(new TrekModel("Manaslu Circuit", "Gorkha, Nepal", "14 Days", R.drawable.mardihimal, "4.9", 60, "Difficult", "5,106m", "177 km", "A true wilderness experience.", "Rs. 28,000"));

        TrekAdapter adapter = new TrekAdapter(trekList);
        recyclerTreks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerTreks.setAdapter(adapter);
    }

    private void setupGearRecyclerView() {

        gearList = new ArrayList<>();

        gearList.add(new GearModel("Down Jacket", "Rs. 2,000", "/ week", R.drawable.jacket));
        gearList.add(new GearModel("Camping Tent", "Rs. 2,500", "/ week", R.drawable.tent));
        gearList.add(new GearModel("Trekking Boots", "Rs. 1,500", "/ week", R.drawable.boots));
        gearList.add(new GearModel("Trekking Poles", "Rs. 500", "/ week", R.drawable.poles));
        gearList.add(new GearModel("Trekking Gloves", "Rs. 400", "/ week", R.drawable.gloves));
        gearList.add(new GearModel("Backpack 60L", "Rs. 1,800", "/ week", R.drawable.backpack));
        gearList.add(new GearModel("Sleeping Bag", "Rs. 1,000", "/ week", R.drawable.sleepingbag));

        GearAdapter adapter = new GearAdapter(gearList);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        recyclerGear.setLayoutManager(layoutManager);
        recyclerGear.setAdapter(adapter);
        recyclerGear.setNestedScrollingEnabled(false);
    }

    private void setupPartnerRecyclerView() {
        partnerList = new ArrayList<>();
        partnerList.add(new PartnerModel("Nirajan Tamang", "5.0", "(32)", "Available", R.drawable.partner1));
        partnerList.add(new PartnerModel("Pema Sherpa", "4.9", "(28)", "Available", R.drawable.partner2));
        partnerList.add(new PartnerModel("Ramesh Gurung", "4.8", "(21)", "Available", R.drawable.partner3));
        partnerList.add(new PartnerModel("Dawa Lama", "4.9", "(19)", "Available", R.drawable.partner4));

        PartnerAdapter adapter = new PartnerAdapter(partnerList);
        recyclerPartners.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerPartners.setAdapter(adapter);
    }

    private void setupPostRecyclerView(){
        postList = new ArrayList<>(PostRepository.getAllPosts());
        postAdapter = new PostAdapter(postList);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerPosts.setAdapter(postAdapter);
    }

    private void setupBottomNavigation() {
        if (bottomNavigation == null) return; // Null check for included layout views
        
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_gear) {
                try {
                    startActivity(new Intent(this, GearRentalActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (id == R.id.nav_partner) {
                try {
                    startActivity(new Intent(this, PartnerFinderActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (id == R.id.nav_profile) {
                if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
            return true;
        });
    }

    private void setupUserMenu() {
        if (drawerLayout == null) return;

        // Header User Data
        String userId = SessionUser.getUserId(this);
        ((TextView) findViewById(R.id.menuUserName)).setText("Bibek Paudel"); // Placeholder
        ((TextView) findViewById(R.id.menuUserHandle)).setText("@" + userId.toLowerCase().replace(" ", ""));

        findViewById(R.id.profileHeader).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // Account Options
        setupMenuOption(R.id.optionProfile, R.drawable.ic_person, "Profile", "View and edit your profile", ProfileActivity.class);
        setupMenuOption(R.id.optionAccountInfo, R.drawable.ic_info_purple, "Account Information", "Personal account details", AccountInformationActivity.class);
        setupMenuOption(R.id.optionSettings, R.drawable.ic_settings_purple, "Settings", "App and account settings", SettingsActivity.class);

        // Activity Options
        setupMenuOption(R.id.optionMyBookings, R.drawable.ic_calendar_purple, "My Bookings", "Your trek, gear & guide bookings", MyBookingsActivity.class);
        setupMenuOption(R.id.optionMyGears, R.drawable.ic_gear_purple, "My Gears", "Gear you have rented", MyGearsActivity.class);
        setupMenuOption(R.id.optionMyPostedGears, R.drawable.ic_tag_purple, "My Posted Gears", "Gear you posted for rental", MyPostedGearsActivity.class);
        setupMenuOption(R.id.optionRecentPosts, R.drawable.ic_chat, "Recent Posts", "Browse latest trek partner requests", TrekPostsActivity.class);
        setupMenuOption(R.id.optionMyTrekPosts, R.drawable.ic_mountain_purple, "My Trek Posts", "Your partner & trek posts", MyTrekPostsActivity.class);
        setupMenuOption(R.id.optionMyTrekPartners, R.drawable.ic_people_purple, "My Trek Partners", "Your trekking connections", MyTrekPartnersActivity.class);
        setupMenuOption(R.id.optionMyTrekRequests, R.drawable.ic_mail_purple, "My Trek Requests", "Requests you sent to join treks", MyRequestsActivity.class);

        // Logout
        View logoutView = findViewById(R.id.optionLogout);
        setupMenuOptionUI(logoutView, R.drawable.ic_logout, "Logout", "Sign out from your account");
        logoutView.findViewById(R.id.iconContainer).setBackgroundResource(R.drawable.bg_circle_red);
        ((TextView) logoutView.findViewById(R.id.menuTitle)).setTextColor(getResources().getColor(R.color.logout_red));
        logoutView.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void setupMenuOption(int viewId, int iconRes, String title, String desc, Class<?> activityClass) {
        View view = findViewById(viewId);
        setupMenuOptionUI(view, iconRes, title, desc);
        view.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            startActivity(new Intent(this, activityClass));
        });
    }

    private void setupMenuOptionUI(View view, int iconRes, String title, String desc) {
        ((ImageView) view.findViewById(R.id.menuIcon)).setImageResource(iconRes);
        ((TextView) view.findViewById(R.id.menuTitle)).setText(title);
        ((TextView) view.findViewById(R.id.menuDescription)).setText(desc);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to logout from TrekMate Nepal?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    SessionUser.setUserId(this, ""); // Clear user session
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clickListeners() {
        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, PostPartnerRequestActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cardRentGear.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, GearRentalActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        cardPartner.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, PartnerFinderActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        cardPostGear.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, PostGearActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        cardTreks.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, TrekPackageActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        notificationBtn.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, NotificationActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        profileImage.setOnClickListener(v -> {
            if (drawerLayout != null) drawerLayout.openDrawer(GravityCompat.END);
        });
        
        exploreBtn.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, TrekListActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.fabChatContainer).setOnClickListener(v -> {
            try {
                ChatBottomSheetFragment chatBottomSheet = new ChatBottomSheetFragment();
                chatBottomSheet.show(getSupportFragmentManager(), chatBottomSheet.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        viewAllTreks.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, TrekListActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        viewAllGear.setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, GearRentalActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        findViewById(R.id.viewAllPosts).setOnClickListener(v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, TrekPostsActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Category clicks
        View.OnClickListener openCategory = v -> {
            try {
                startActivity(new Intent(DashboardActivity.this, CategoryActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        findViewById(R.id.categoryTrekking).setOnClickListener(openCategory);
        findViewById(R.id.categoryCamping).setOnClickListener(openCategory);
        findViewById(R.id.categoryHiking).setOnClickListener(openCategory);
        findViewById(R.id.categoryClimbing).setOnClickListener(openCategory);
        findViewById(R.id.categoryAdventure).setOnClickListener(openCategory);
    }
}