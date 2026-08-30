package com.example.trekmatenepal.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.ChatAdapter;
import com.example.trekmatenepal.models.ChatMessageModel;
import com.example.trekmatenepal.models.PartnerModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ChatActivity — full local chat screen with media support.
 */
public class ChatActivity extends AppCompatActivity {

    private ImageView    btnBack, imgPartner, btnSend, btnAttach, btnCamera, btnCall, btnMore, btnRemovePreview, imgPreview;
    private TextView     tvPartnerName, tvOnlineStatus, txtPreviewName;
    private View         partnerHeaderInfo;
    private EditText     etMessage;
    private RecyclerView recyclerMessages;
    private LinearLayout attachmentPreview;

    private List<ChatMessageModel> messages = new ArrayList<>();
    private ChatAdapter            adapter;
    private final SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    private Uri    stagedAttachmentUri;
    private String stagedAttachmentName;
    private String stagedAttachmentType;

    // Activity Result Launchers
    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openCamera();
                else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    stageAttachment(null, "Captured Image", "image");
                }
            });

    private final ActivityResultLauncher<String> pickFileLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    String type = getContentResolver().getType(uri);
                    String category = "file";
                    if (type != null) {
                        if (type.startsWith("image")) category = "image";
                        else if (type.startsWith("video")) category = "video";
                    }
                    stageAttachment(uri, uri.getLastPathSegment(), category);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        loadPartnerData();
        loadSampleMessages();
        setupRecycler();
        setupClickListeners();
    }

    private void initViews() {
        btnBack           = findViewById(R.id.btnBack);
        imgPartner        = findViewById(R.id.imgPartner);
        partnerHeaderInfo = findViewById(R.id.partnerHeaderInfo);
        tvPartnerName     = findViewById(R.id.tvPartnerName);
        tvOnlineStatus    = findViewById(R.id.tvOnlineStatus);
        etMessage         = findViewById(R.id.etMessage);
        recyclerMessages  = findViewById(R.id.recyclerMessages);
        btnSend           = findViewById(R.id.btnSend);
        btnAttach         = findViewById(R.id.btnAttach);
        btnCamera         = findViewById(R.id.btnCamera);
        btnCall           = findViewById(R.id.btnCall);
        btnMore           = findViewById(R.id.btnMore);
        
        attachmentPreview = findViewById(R.id.attachmentPreview);
        imgPreview        = findViewById(R.id.imgPreview);
        txtPreviewName    = findViewById(R.id.txtPreviewName);
        btnRemovePreview  = findViewById(R.id.btnRemovePreview);
    }

    private void loadPartnerData() {
        String  name    = getIntent().getStringExtra("partnerName");
        int     image   = getIntent().getIntExtra("partnerImage", R.drawable.partner1);
        boolean online  = getIntent().getBooleanExtra("isOnline", true);

        tvPartnerName.setText(name != null ? name : "Trek Partner");
        imgPartner.setImageResource(image != 0 ? image : R.drawable.partner1);

        tvOnlineStatus.setText(online ? "🟢 Online" : "⚫ Offline");
        tvOnlineStatus.setTextColor(getResources().getColor(
                online ? R.color.success_green : R.color.secondary_gray));
    }

    private void loadSampleMessages() {
        String partnerName = tvPartnerName.getText().toString();
        String firstName   = partnerName.contains(" ") ? partnerName.split(" ")[0] : partnerName;

        messages.add(new ChatMessageModel("Hi Bibek! 👋 I received your request.", "10:30 AM", ChatMessageModel.TYPE_RECEIVED));
        messages.add(new ChatMessageModel("Hi " + firstName + "! 😊 Great to hear that.", "10:31 AM", ChatMessageModel.TYPE_SENT));
    }

    private void setupRecycler() {
        adapter = new ChatAdapter(messages);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerMessages.setLayoutManager(lm);
        recyclerMessages.setAdapter(adapter);
        scrollToBottom();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> goBackToChatSection());
        btnCall.setOnClickListener(v -> makeAudioCall());
        btnMore.setOnClickListener(v -> showMoreOptions());
        imgPartner.setOnClickListener(v -> openPartnerProfile());
        partnerHeaderInfo.setOnClickListener(v -> openPartnerProfile());
        btnAttach.setOnClickListener(v -> pickFile());
        btnCamera.setOnClickListener(v -> checkCameraPermission());
        btnSend.setOnClickListener(v -> sendMessage());
        btnRemovePreview.setOnClickListener(v -> clearStagedAttachment());
    }

    private void stageAttachment(Uri uri, String name, String type) {
        stagedAttachmentUri = uri;
        stagedAttachmentName = name;
        stagedAttachmentType = type;

        attachmentPreview.setVisibility(View.VISIBLE);
        txtPreviewName.setText(name);
        if ("image".equals(type) && uri != null) {
            imgPreview.setVisibility(View.VISIBLE);
            imgPreview.setImageURI(uri);
        } else {
            imgPreview.setVisibility(View.GONE);
        }
    }

    private void clearStagedAttachment() {
        stagedAttachmentUri = null;
        stagedAttachmentName = null;
        stagedAttachmentType = null;
        attachmentPreview.setVisibility(View.GONE);
    }

    private void goBackToChatSection() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("open_chat", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void makeAudioCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+9779812345678"));
        startActivity(intent);
    }

    private void openPartnerProfile() {
        String name = tvPartnerName.getText().toString();
        int imageRes = getIntent().getIntExtra("partnerImage", R.drawable.partner1);
        PartnerModel partner = new PartnerModel(name, "4.8", "(24)", "Available", imageRes);
        partner.setDestination("Everest Base Camp");
        Intent intent = new Intent(this, PartnerProfileActivity.class);
        intent.putExtra("partner", partner);
        startActivity(intent);
    }

    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, btnMore);
        popup.getMenu().add("Send Emoji");
        popup.getMenu().add("Change Background Theme");
        popup.getMenu().add("Set Nickname");
        popup.getMenu().add("Open Profile");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Send Emoji": etMessage.append("😊"); break;
                case "Change Background Theme":
                    findViewById(android.R.id.content).setBackgroundColor(ContextCompat.getColor(this, R.color.purple_light));
                    break;
                case "Set Nickname": tvPartnerName.setText("Best Buddy 🏔️"); break;
                case "Open Profile": openPartnerProfile(); break;
            }
            return true;
        });
        popup.show();
    }

    private void pickFile() {
        pickFileLauncher.launch("*/*");
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(takePictureIntent);
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty() && stagedAttachmentUri == null && stagedAttachmentName == null) return;

        String time = timeFmt.format(new Date());
        ChatMessageModel newMessage;
        if (stagedAttachmentName != null) {
            newMessage = new ChatMessageModel(text, time, ChatMessageModel.TYPE_SENT, stagedAttachmentUri, stagedAttachmentName, stagedAttachmentType);
        } else {
            newMessage = new ChatMessageModel(text, time, ChatMessageModel.TYPE_SENT);
        }
        
        messages.add(newMessage);
        adapter.notifyItemInserted(messages.size() - 1);
        etMessage.setText("");
        clearStagedAttachment();
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (!messages.isEmpty()) {
            recyclerMessages.smoothScrollToPosition(messages.size() - 1);
        }
    }
}
