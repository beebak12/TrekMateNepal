package com.example.trekmatenepal.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.PostRepository;
import com.example.trekmatenepal.data.PartnerPostBackendRepository;
import com.example.trekmatenepal.data.ChatBackendRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.ChatSummaryModel;
import com.example.trekmatenepal.models.PostModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostPartnerRequestActivity extends AppCompatActivity {

    private Spinner spinnerTrek, spinnerPartners, spinnerExperience;
    private TextInputLayout tilCustomTrek;
    private EditText etCustomTrek;
    private LinearLayout layoutStartDate, layoutEndDate;
    private TextView tvStartDate, tvEndDate;
    private EditText etLocation, etBudget, etAbout, etGroupName;
    private FrameLayout layoutSelectRouteImage;
    private ImageView imgRoutePreview;
    private LinearLayout layoutAddImageIcon;
    private Button btnPost;

    private Uri selectedImageUri;

    private final ActivityResultLauncher<String[]> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        selectedImageUri = uri;
                        imgRoutePreview.setImageURI(uri);
                        imgRoutePreview.setVisibility(View.VISIBLE);
                        layoutAddImageIcon.setVisibility(View.GONE);
                    } catch (SecurityException error) {
                        selectedImageUri = null;
                        Toast.makeText(this, "This image cannot be saved. Please choose another image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_partner_request);

        initViews();
        setupSpinners();
        setupDatePickers();
        setupListeners();
    }

    private void initViews() {
        findViewById(R.id.btnMenu).setOnClickListener(v -> finish());
        spinnerTrek = findViewById(R.id.spinnerTrek);
        tilCustomTrek = findViewById(R.id.tilCustomTrek);
        etCustomTrek = findViewById(R.id.etCustomTrek);
        spinnerPartners = findViewById(R.id.spinnerPartners);
        spinnerExperience = findViewById(R.id.spinnerExperience);
        layoutStartDate = findViewById(R.id.layoutStartDate);
        layoutEndDate = findViewById(R.id.layoutEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        etLocation = findViewById(R.id.etLocation);
        etBudget = findViewById(R.id.etBudget);
        etAbout = findViewById(R.id.etAbout);
        etGroupName = findViewById(R.id.etGroupName);
        layoutSelectRouteImage = findViewById(R.id.layoutSelectRouteImage);
        imgRoutePreview = findViewById(R.id.imgRoutePreview);
        layoutAddImageIcon = findViewById(R.id.layoutAddImageIcon);
        btnPost = findViewById(R.id.btnPost);
    }

    private void setupSpinners() {
        List<String> treks = new ArrayList<>();
        treks.add("Everest Base Camp (EBC)");
        treks.add("Annapurna Circuit");
        treks.add("Langtang Valley");
        treks.add("Mardi Himal");
        treks.add("Manaslu Circuit");
        treks.add("Other (Custom)");

        ArrayAdapter<String> trekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, treks);
        trekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrek.setAdapter(trekAdapter);

        spinnerTrek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (treks.get(position).equals("Other (Custom)")) {
                    tilCustomTrek.setVisibility(View.VISIBLE);
                } else {
                    tilCustomTrek.setVisibility(View.GONE);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] partners = {"1", "2", "3", "4", "5", "5+"};
        ArrayAdapter<String> partnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partners);
        partnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPartners.setAdapter(partnerAdapter);

        String[] experience = {"Beginner", "Moderate", "Expert"};
        ArrayAdapter<String> expAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, experience);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperience.setAdapter(expAdapter);
    }

    private void setupDatePickers() {
        layoutStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        layoutEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));
    }

    private void showDatePicker(TextView tv) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + " " + getMonthName(month) + " " + year;
            tv.setText(date);
            tv.setTextColor(getResources().getColor(R.color.dark_text));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    private void setupListeners() {
        layoutSelectRouteImage.setOnClickListener(v -> pickImageLauncher.launch(new String[]{"image/*"}));

        btnPost.setOnClickListener(v -> {
            String trek = spinnerTrek.getSelectedItem().toString();
            if (trek.equals("Other (Custom)")) {
                trek = etCustomTrek.getText().toString().trim();
                if (trek.isEmpty()) {
                    etCustomTrek.setError("Enter trek name");
                    return;
                }
            }

            String startDate = tvStartDate.getText().toString();
            String endDate = tvEndDate.getText().toString();
            String partnersNeeded = spinnerPartners.getSelectedItem().toString();
            String location = etLocation.getText().toString().trim();
            String budget = etBudget.getText().toString().trim();
            String exp = spinnerExperience.getSelectedItem().toString();
            String about = etAbout.getText().toString().trim();
            String groupName = etGroupName.getText().toString().trim();

            if (startDate.equals("Select Date") || endDate.equals("Select Date")
                    || location.isEmpty() || groupName.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                if (groupName.isEmpty()) etGroupName.setError("Enter a chat group name");
                return;
            }

            String currentUserId = SessionUser.getUserId(this);

            PostModel newPost = new PostModel(
                    trek,
                    currentUserId, // Using userId as author name for now or "Bibek"
                    location,
                    startDate + " – " + endDate,
                    "TBD",
                    partnersNeeded,
                    R.drawable.everest
            );
            newPost.setAuthorId(currentUserId);
            newPost.setDescription(about);
            newPost.setBudget(budget);
            newPost.setExperienceLevel(exp);
            newPost.setGroupName(groupName);
            if (selectedImageUri != null) {
                newPost.setCustomImageUri(selectedImageUri.toString());
            }

            PostRepository.addPost(this, newPost);

            int requiredCount;
            try { requiredCount = Integer.parseInt(partnersNeeded.replace("+", "")); } catch (Exception ignored) { requiredCount = 5; }

            // Keep the group immediately visible offline; the server group is created after the post receives its database id.
            ChatSummaryModel groupChat = new ChatSummaryModel(
                    newPost.getGroupId(), groupName, "Group created. Welcome!", "Just now",
                    newPost.getImageRes(), 0, true);
            if (newPost.getCustomImageUri() != null) groupChat.setCustomImageUri(newPost.getCustomImageUri());
            groupChat.setAdminId(currentUserId);
            groupChat.addMember(currentUserId);
            com.example.trekmatenepal.data.ChatRepository.loadChats(this);
            com.example.trekmatenepal.data.ChatRepository.addChat(this, groupChat);

            PartnerPostBackendRepository.create(this, newPost, startDate, requiredCount, new PartnerPostBackendRepository.CallbackResult() {
                @Override public void success() {
                    if (newPost.getId().matches("\\d+")) {
                        ChatBackendRepository.createGroup(PostPartnerRequestActivity.this, groupName, Integer.parseInt(newPost.getId()), new ChatBackendRepository.GroupCallback() {
                            @Override public void success(int groupId) {
                                String localGroupId = newPost.getGroupId();
                                newPost.setGroupId(String.valueOf(groupId));
                                PostRepository.persist(PostPartnerRequestActivity.this);
                                com.example.trekmatenepal.data.ChatRepository.replaceChatId(PostPartnerRequestActivity.this, localGroupId, String.valueOf(groupId));
                            }
                            @Override public void error(String message) { }
                        });
                    }
                }
                @Override public void error(String message) { Toast.makeText(PostPartnerRequestActivity.this, message, Toast.LENGTH_SHORT).show(); }
            });

            Toast.makeText(this, "Trek request posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
