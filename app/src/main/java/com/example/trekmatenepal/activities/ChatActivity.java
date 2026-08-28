package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.MessageAdapter;
import com.example.trekmatenepal.models.MessageModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private EditText editMessage;
    private MaterialButton btnSend;
    private TextView btnBack;
    private TextView txtChatTitle;
    private TextView txtMemberCount;

    private ArrayList<MessageModel> messageList;
    private MessageAdapter messageAdapter;

    private String selectedChatName;
    private String selectedChatSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        loadSelectedChat();
        setupMessages();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerMessages =
                findViewById(R.id.recyclerMessages);

        editMessage =
                findViewById(R.id.editMessage);

        btnSend =
                findViewById(R.id.btnSend);

        btnBack =
                findViewById(R.id.btnBack);

        txtChatTitle =
                findViewById(R.id.txtChatTitle);

        txtMemberCount =
                findViewById(R.id.txtMemberCount);
    }

    private void loadSelectedChat() {
        selectedChatName =
                getIntent().getStringExtra("chat_name");

        selectedChatSubtitle =
                getIntent().getStringExtra("chat_subtitle");

        if (selectedChatName == null
                || selectedChatName.trim().isEmpty()) {
            selectedChatName = "Annapurna Trek Group";
        }

        if (selectedChatSubtitle == null
                || selectedChatSubtitle.trim().isEmpty()) {
            selectedChatSubtitle = "4 members";
        }

        txtChatTitle.setText(selectedChatName);
        txtMemberCount.setText(selectedChatSubtitle);
    }

    private void setupMessages() {
        messageList = new ArrayList<>();

        if (selectedChatName.equals(
                "Annapurna Trek Group"
        )) {
            addGroupSampleMessages();
        } else {
            addDirectSampleMessages();
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);

        recyclerMessages.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(messageList);
        recyclerMessages.setAdapter(messageAdapter);

        if (!messageList.isEmpty()) {
            recyclerMessages.scrollToPosition(
                    messageList.size() - 1
            );
        }
    }

    private void addGroupSampleMessages() {
        messageList.add(new MessageModel(
                "Sam",
                "Hi! Is everyone ready for the Annapurna trek?",
                "10:25 AM",
                false,
                R.drawable.partner1
        ));

        messageList.add(new MessageModel(
                "You",
                "Yes, I have already packed most of my gear.",
                "10:27 AM",
                true,
                0
        ));

        messageList.add(new MessageModel(
                "Alex",
                "What time should we meet tomorrow?",
                "10:30 AM",
                false,
                R.drawable.partner2
        ));

        messageList.add(new MessageModel(
                "You",
                "Let’s meet at 7 AM near the bus park.",
                "10:32 AM",
                true,
                0
        ));
    }

    private void addDirectSampleMessages() {
        messageList.add(new MessageModel(
                selectedChatName,
                "Hello! How can I help you?",
                "9:15 AM",
                false,
                R.drawable.partner1
        ));

        messageList.add(new MessageModel(
                "You",
                "Hi, I wanted to ask you about the details.",
                "9:17 AM",
                true,
                0
        ));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(
                view -> finish()
        );

        btnSend.setOnClickListener(
                view -> sendMessage()
        );
    }

    private void sendMessage() {
        String messageText =
                editMessage.getText().toString().trim();

        if (messageText.isEmpty()) {
            return;
        }

        String currentTime = new SimpleDateFormat(
                "h:mm a",
                Locale.getDefault()
        ).format(new Date());

        MessageModel newMessage = new MessageModel(
                "You",
                messageText,
                currentTime,
                true,
                0
        );

        messageList.add(newMessage);

        int newMessagePosition =
                messageList.size() - 1;

        messageAdapter.notifyItemInserted(
                newMessagePosition
        );

        recyclerMessages.scrollToPosition(
                newMessagePosition
        );

        editMessage.setText("");
    }
}