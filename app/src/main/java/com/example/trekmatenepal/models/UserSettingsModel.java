package com.example.trekmatenepal.models;

import java.io.Serializable;

public class UserSettingsModel implements Serializable {
    private String userId;

    // Notifications
    private boolean pushNotifications = true;
    private boolean trekRequests = true;
    private boolean partnerRequests = true;
    private boolean bookingUpdates = true;
    private boolean gearRentalUpdates = true;
    private boolean chatMessages = true;
    private boolean promotionalNotifications = false;

    // Privacy
    private String profileVisibility = "Everyone"; // Everyone, Trek Partners Only, Private
    private boolean showContactNumber = true;
    private boolean showEmailAddress = true;
    private String whoCanMessage = "Everyone"; // Everyone, My Trek Partners, Nobody
    private String whoCanSendTrekRequests = "Everyone"; // Everyone, Users on My Treks, Nobody
    private boolean showOnlineStatus = true;

    // App Preferences
    private String language = "English";
    private String appearance = "System Default"; // System Default, Light, Dark
    private boolean locationServices = true;
    private boolean autoPlayImages = true;

    public UserSettingsModel() {}

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isPushNotifications() { return pushNotifications; }
    public void setPushNotifications(boolean pushNotifications) { this.pushNotifications = pushNotifications; }

    public boolean isTrekRequests() { return trekRequests; }
    public void setTrekRequests(boolean trekRequests) { this.trekRequests = trekRequests; }

    public boolean isPartnerRequests() { return partnerRequests; }
    public void setPartnerRequests(boolean partnerRequests) { this.partnerRequests = partnerRequests; }

    public boolean isBookingUpdates() { return bookingUpdates; }
    public void setBookingUpdates(boolean bookingUpdates) { this.bookingUpdates = bookingUpdates; }

    public boolean isGearRentalUpdates() { return gearRentalUpdates; }
    public void setGearRentalUpdates(boolean gearRentalUpdates) { this.gearRentalUpdates = gearRentalUpdates; }

    public boolean isChatMessages() { return chatMessages; }
    public void setChatMessages(boolean chatMessages) { this.chatMessages = chatMessages; }

    public boolean isPromotionalNotifications() { return promotionalNotifications; }
    public void setPromotionalNotifications(boolean promotionalNotifications) { this.promotionalNotifications = promotionalNotifications; }

    public String getProfileVisibility() { return profileVisibility; }
    public void setProfileVisibility(String profileVisibility) { this.profileVisibility = profileVisibility; }

    public boolean isShowContactNumber() { return showContactNumber; }
    public void setShowContactNumber(boolean showContactNumber) { this.showContactNumber = showContactNumber; }

    public boolean isShowEmailAddress() { return showEmailAddress; }
    public void setShowEmailAddress(boolean showEmailAddress) { this.showEmailAddress = showEmailAddress; }

    public String getWhoCanMessage() { return whoCanMessage; }
    public void setWhoCanMessage(String whoCanMessage) { this.whoCanMessage = whoCanMessage; }

    public String getWhoCanSendTrekRequests() { return whoCanSendTrekRequests; }
    public void setWhoCanSendTrekRequests(String whoCanSendTrekRequests) { this.whoCanSendTrekRequests = whoCanSendTrekRequests; }

    public boolean isShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getAppearance() { return appearance; }
    public void setAppearance(String appearance) { this.appearance = appearance; }

    public boolean isLocationServices() { return locationServices; }
    public void setLocationServices(boolean locationServices) { this.locationServices = locationServices; }

    public boolean isAutoPlayImages() { return autoPlayImages; }
    public void setAutoPlayImages(boolean autoPlayImages) { this.autoPlayImages = autoPlayImages; }
}
