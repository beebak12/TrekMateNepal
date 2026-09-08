package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.NotificationAdapter;
import com.example.trekmatenepal.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;

    private LinearLayout emptyState;

    private LinearLayout tabAll;
    private LinearLayout tabBookings;
    private LinearLayout tabChats;

    private View tabIndicator;

    private TextView tvAllCount;
    private TextView tvBookingsCount;
    private TextView tvChatsCount;

    private NotificationAdapter adapter;

    private final List<NotificationModel> allNotifications =
            new ArrayList<>();

    private final List<NotificationModel> displayedNotifications =
            new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        initializeViews();

        createSampleNotifications();

        showAllNotifications();

        setupClickListeners();

        updateCounts();
    }


    // ==========================================
    // INITIALIZE VIEWS
    // ==========================================

    private void initializeViews() {

        recyclerNotifications =
                findViewById(R.id.recyclerNotifications);

        emptyState =
                findViewById(R.id.emptyState);

        tabAll =
                findViewById(R.id.tabAll);

        tabBookings =
                findViewById(R.id.tabBookings);

        tabChats =
                findViewById(R.id.tabChats);

        tabIndicator =
                findViewById(R.id.tabIndicator);

        tvAllCount =
                findViewById(R.id.tvAllCount);

        tvBookingsCount =
                findViewById(R.id.tvBookingsCount);

        tvChatsCount =
                findViewById(R.id.tvChatsCount);


        recyclerNotifications.setLayoutManager(
                new LinearLayoutManager(this)
        );


        adapter =
                new NotificationAdapter(
                        displayedNotifications
                );


        recyclerNotifications.setAdapter(adapter);
    }


    // ==========================================
    // SAMPLE NOTIFICATIONS
    // ==========================================

    private void createSampleNotifications() {
        allNotifications.clear();

        // 1. Partner Request
        allNotifications.add(new NotificationModel(1, "partner_request", "Partner Request", "Ram Sherpa wants to join your Everest Base Camp trek.", "10 min ago", false));

        // 2. Chat/New Message
        allNotifications.add(new NotificationModel(2, "chat", "New Message", "Sujan: Are you available for a quick call?", "30 min ago", false));

        // 3. Request Accepted
        allNotifications.add(new NotificationModel(3, "accepted", "Request Accepted", "Pema accepted your request to join the Langtang trek.", "2 hours ago", true));

        // 4. Package Booking
        allNotifications.add(new NotificationModel(4, "booking", "Package Booked", "A new trekker booked your Annapurna package.", "Yesterday", false));

        // 5. Rental Request
        allNotifications.add(new NotificationModel(5, "rental", "Gear Rental Request", "Bibek wants to rent your trekking poles.", "Yesterday", true));

        // 6. General Notification
        allNotifications.add(new NotificationModel(6, "general", "Welcome to TrekMate", "Complete your profile to find better partners.", "2 days ago", true));
    }


    // ==========================================
    // SHOW ALL
    // ==========================================

    private void showAllNotifications() {

        displayedNotifications.clear();

        displayedNotifications.addAll(
                allNotifications
        );

        adapter.notifyDataSetChanged();

        updateEmptyState();

        setActiveTab("all");
    }


    // ==========================================
    // SHOW BOOKINGS
    // ==========================================

    private void showBookingNotifications() {

        displayedNotifications.clear();

        for (NotificationModel notification :
                allNotifications) {

            if (notification.getType().equals("booking")
                    || notification.getTitle()
                    .toLowerCase()
                    .contains("booking")) {

                displayedNotifications.add(notification);
            }
        }

        adapter.notifyDataSetChanged();

        updateEmptyState();

        setActiveTab("bookings");
    }


    // ==========================================
    // SHOW CHATS
    // ==========================================

    private void showChatNotifications() {

        displayedNotifications.clear();

        for (NotificationModel notification :
                allNotifications) {

            if (notification.getType().equals("chat")) {

                displayedNotifications.add(notification);
            }
        }

        adapter.notifyDataSetChanged();

        updateEmptyState();

        setActiveTab("chats");
    }


    // ==========================================
    // EMPTY STATE
    // ==========================================

    private void updateEmptyState() {

        if (displayedNotifications.isEmpty()) {

            recyclerNotifications.setVisibility(
                    View.GONE
            );

            emptyState.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerNotifications.setVisibility(
                    View.VISIBLE
            );

            emptyState.setVisibility(
                    View.GONE
            );
        }
    }


    // ==========================================
    // UPDATE COUNTS
    // ==========================================

    private void updateCounts() {

        int allUnread = 0;
        int bookingUnread = 0;
        int chatUnread = 0;


        for (NotificationModel notification :
                allNotifications) {

            if (!notification.isRead()) {

                allUnread++;

                if (notification.getType()
                        .equals("booking")) {

                    bookingUnread++;
                }

                if (notification.getType()
                        .equals("chat")) {

                    chatUnread++;
                }
            }
        }


        tvAllCount.setText(
                String.valueOf(allUnread)
        );

        tvBookingsCount.setText(
                String.valueOf(bookingUnread)
        );

        tvChatsCount.setText(
                String.valueOf(chatUnread)
        );
    }


    // ==========================================
    // CLICK LISTENERS
    // ==========================================

    private void setupClickListeners() {

        // Back

        ImageView btnBack =
                findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                finish()
        );


        // All

        tabAll.setOnClickListener(v -> {

            showAllNotifications();

            updateCounts();
        });


        // Bookings

        tabBookings.setOnClickListener(v -> {

            showBookingNotifications();

            updateCounts();
        });


        // Chats

        tabChats.setOnClickListener(v -> {

            showChatNotifications();

            updateCounts();
        });


        // More menu

        ImageView btnMore =
                findViewById(R.id.btnMore);

        btnMore.setOnClickListener(v -> {

            PopupMenu popupMenu =
                    new PopupMenu(
                            NotificationActivity.this,
                            btnMore
                    );

            popupMenu.getMenu().add(
                    "Mark all as read"
            );


            popupMenu.setOnMenuItemClickListener(
                    item -> {

                        adapter.markAllAsRead();

                        updateCounts();

                        return true;
                    }
            );


            popupMenu.show();
        });
    }


    // ==========================================
    // ACTIVE TAB
    // ==========================================

    private void setActiveTab(String selectedTab) {

        TextView all =
                findViewById(R.id.tvAll);

        TextView bookings =
                findViewById(R.id.tvBookings);

        TextView chats =
                findViewById(R.id.tvChats);


        // Reset

        all.setTextColor(
                getColor(R.color.text_secondary)
        );

        bookings.setTextColor(
                getColor(R.color.text_secondary)
        );

        chats.setTextColor(
                getColor(R.color.text_secondary)
        );


        if (selectedTab.equals("all")) {

            all.setTextColor(
                    getColor(R.color.purple)
            );

            all.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            moveIndicator(0);

        } else if (selectedTab.equals("bookings")) {

            bookings.setTextColor(
                    getColor(R.color.purple)
            );

            bookings.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            moveIndicator(1);

        } else {

            chats.setTextColor(
                    getColor(R.color.purple)
            );

            chats.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );

            moveIndicator(2);
        }
    }


    // ==========================================
    // MOVE TAB INDICATOR
    // ==========================================

    private void moveIndicator(int position) {

        LinearLayout container =
                findViewById(R.id.tabContainer);

        container.post(() -> {

            int tabWidth =
                    container.getWidth() / 3;

            float targetX =
                    position * tabWidth
                            + (tabWidth - tabIndicator.getWidth()) / 2f
                            - 42;

            tabIndicator.animate()
                    .translationX(targetX)
                    .setDuration(180)
                    .start();
        });
    }
}