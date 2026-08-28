package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.ChatListAdapter;
import com.example.trekmatenepal.models.ChatListModel;

import java.util.ArrayList;
import java.util.Locale;

public class ChatListActivity extends AppCompatActivity {

    private TextView btnChatListBack;
    private EditText searchChats;
    private RecyclerView recyclerChatList;

    private ArrayList<ChatListModel> allChats;
    private ArrayList<ChatListModel> displayedChats;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initializeViews();
        setupChatList();
        setupSearch();
        setupClickListeners();
    }

    private void initializeViews() {
        btnChatListBack =
                findViewById(R.id.btnChatListBack);

        searchChats =
                findViewById(R.id.searchChats);

        recyclerChatList =
                findViewById(R.id.recyclerChatList);
    }

    private void setupChatList() {
        allChats = new ArrayList<>();
        displayedChats = new ArrayList<>();

        allChats.add(new ChatListModel(
                "Annapurna Trek Group",
                "4 members",
                "Sam: Is everyone ready for tomorrow?",
                "10:30 AM",
                R.drawable.annapurna,
                2
        ));

        allChats.add(new ChatListModel(
                "Priya M.",
                "Trek Organizer",
                "Your Everest Base Camp booking is confirmed.",
                "9:15 AM",
                R.drawable.partner1,
                1
        ));

        allChats.add(new ChatListModel(
                "Nirajan Tamang",
                "Trek Partner",
                "See you at 7 AM near the bus park.",
                "Yesterday",
                R.drawable.partner2,
                0
        ));

        allChats.add(new ChatListModel(
                "Gear Support",
                "Rental Provider",
                "Your down jacket is ready for pickup.",
                "Monday",
                R.drawable.jacket,
                0
        ));

        displayedChats.addAll(allChats);

        recyclerChatList.setLayoutManager(
                new LinearLayoutManager(this)
        );

        chatListAdapter = new ChatListAdapter(
                displayedChats,
                this::openSelectedChat
        );

        recyclerChatList.setAdapter(chatListAdapter);
    }

    private void setupSearch() {
        searchChats.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence text,
                            int start,
                            int count,
                            int after
                    ) {
                        // No action required
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence text,
                            int start,
                            int before,
                            int count
                    ) {
                        filterChats(text.toString());
                    }

                    @Override
                    public void afterTextChanged(
                            Editable editable
                    ) {
                        // No action required
                    }
                }
        );
    }

    private void filterChats(String query) {
        displayedChats.clear();

        String normalizedQuery =
                query.trim().toLowerCase(Locale.ROOT);

        if (normalizedQuery.isEmpty()) {
            displayedChats.addAll(allChats);
        } else {
            for (ChatListModel chat : allChats) {
                String chatName = chat
                        .getChatName()
                        .toLowerCase(Locale.ROOT);

                if (chatName.contains(normalizedQuery)) {
                    displayedChats.add(chat);
                }
            }
        }

        chatListAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        btnChatListBack.setOnClickListener(
                view -> finish()
        );
    }

    private void openSelectedChat(ChatListModel chat) {
        Intent intent = new Intent(
                ChatListActivity.this,
                ChatActivity.class
        );

        intent.putExtra(
                "chat_name",
                chat.getChatName()
        );

        intent.putExtra(
                "chat_subtitle",
                chat.getChatSubtitle()
        );

        startActivity(intent);
    }
}