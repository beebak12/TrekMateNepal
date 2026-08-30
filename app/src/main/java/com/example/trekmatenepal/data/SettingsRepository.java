package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.trekmatenepal.models.UserSettingsModel;
import org.json.JSONObject;

public class SettingsRepository {
    private static final String PREF_NAME = "TrekMateSettings";
    private static final String KEY_USER_SETTINGS = "user_settings_json";
    
    private final SharedPreferences sharedPreferences;
    private static SettingsRepository instance;

    private SettingsRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SettingsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsRepository(context.getApplicationContext());
        }
        return instance;
    }

    public UserSettingsModel getSettings() {
        String jsonStr = sharedPreferences.getString(KEY_USER_SETTINGS, null);
        UserSettingsModel settings = new UserSettingsModel();
        if (jsonStr != null) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                settings.setPushNotifications(json.optBoolean("pushNotifications", true));
                settings.setTrekRequests(json.optBoolean("trekRequests", true));
                settings.setPartnerRequests(json.optBoolean("partnerRequests", true));
                settings.setBookingUpdates(json.optBoolean("bookingUpdates", true));
                settings.setGearRentalUpdates(json.optBoolean("gearRentalUpdates", true));
                settings.setChatMessages(json.optBoolean("chatMessages", true));
                settings.setPromotionalNotifications(json.optBoolean("promotionalNotifications", false));
                
                settings.setProfileVisibility(json.optString("profileVisibility", "Everyone"));
                settings.setShowContactNumber(json.optBoolean("showContactNumber", true));
                settings.setShowEmailAddress(json.optBoolean("showEmailAddress", true));
                settings.setWhoCanMessage(json.optString("whoCanMessage", "Everyone"));
                settings.setWhoCanSendTrekRequests(json.optString("whoCanSendTrekRequests", "Everyone"));
                settings.setShowOnlineStatus(json.optBoolean("showOnlineStatus", true));
                
                settings.setLanguage(json.optString("language", "English"));
                settings.setAppearance(json.optString("appearance", "System Default"));
                settings.setLocationServices(json.optBoolean("locationServices", true));
                settings.setAutoPlayImages(json.optBoolean("autoPlayImages", true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return settings;
    }

    public void saveSettings(UserSettingsModel settings) {
        try {
            JSONObject json = new JSONObject();
            json.put("pushNotifications", settings.isPushNotifications());
            json.put("trekRequests", settings.isTrekRequests());
            json.put("partnerRequests", settings.isPartnerRequests());
            json.put("bookingUpdates", settings.isBookingUpdates());
            json.put("gearRentalUpdates", settings.isGearRentalUpdates());
            json.put("chatMessages", settings.isChatMessages());
            json.put("promotionalNotifications", settings.isPromotionalNotifications());
            
            json.put("profileVisibility", settings.getProfileVisibility());
            json.put("showContactNumber", settings.isShowContactNumber());
            json.put("showEmailAddress", settings.isShowEmailAddress());
            json.put("whoCanMessage", settings.getWhoCanMessage());
            json.put("whoCanSendTrekRequests", settings.getWhoCanSendTrekRequests());
            json.put("showOnlineStatus", settings.isShowOnlineStatus());
            
            json.put("language", settings.getLanguage());
            json.put("appearance", settings.getAppearance());
            json.put("locationServices", settings.isLocationServices());
            json.put("autoPlayImages", settings.isAutoPlayImages());
            
            sharedPreferences.edit().putString(KEY_USER_SETTINGS, json.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
