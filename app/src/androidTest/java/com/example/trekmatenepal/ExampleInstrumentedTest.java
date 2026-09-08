package com.example.trekmatenepal;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.trekmatenepal.data.ChatRepository;
import com.example.trekmatenepal.data.GearFavouriteRepository;
import com.example.trekmatenepal.data.GearRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.ChatSummaryModel;
import com.example.trekmatenepal.models.RentalGearModel;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.trekmatenepal", appContext.getPackageName());
    }

    @Test
    public void favouriteGearPersistsAndTogglesPerAccount() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        context.getSharedPreferences("TrekMatePrefs", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("TrekMateGearFavourites", Context.MODE_PRIVATE).edit().clear().commit();
        SessionUser.setUserId(context, "favourite-test-user");
        RentalGearModel gear = new RentalGearModel(R.drawable.jacket, "Test Jacket", "Clothing",
                "5.0", "Rs. 100 / week", "Available");

        assertTrue(GearFavouriteRepository.toggle(context, gear));
        assertTrue(GearFavouriteRepository.isFavourite(context, gear));
        assertEquals(1, GearFavouriteRepository.getFavourites(context).size());
        assertFalse(GearFavouriteRepository.toggle(context, gear));
        assertFalse(GearFavouriteRepository.isFavourite(context, gear));
    }

    @Test
    public void acceptedMemberCanSeeCreatedGroup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        context.getSharedPreferences("TrekMatePrefs", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("TrekMateChats", Context.MODE_PRIVATE).edit().clear().commit();
        SessionUser.setUserId(context, "admin-user");
        ChatRepository.loadChats(context);
        ChatSummaryModel group = new ChatSummaryModel("test-group", "Test Group", "Created",
                "Now", R.drawable.everest, 0, true);
        group.setAdminId("admin-user");
        group.addMember("admin-user");
        ChatRepository.addChat(context, group);

        SessionUser.setUserId(context, "member-user");
        assertFalse(hasGroup(ChatRepository.getChatsForCurrentUser(context, true), "test-group"));
        ChatRepository.addGroupMember(context, "test-group", "member-user");
        ChatRepository.loadChats(context);
        assertTrue(hasGroup(ChatRepository.getChatsForCurrentUser(context, true), "test-group"));
    }

    @Test
    public void postedGearCanBeAddedUpdatedAndDeleted() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        context.getSharedPreferences("TrekMateGear", Context.MODE_PRIVATE).edit().clear().commit();
        RentalGearModel gear = new RentalGearModel(0, "content://test/gear-image",
                "Test Boots", "Footwear", "New", "Rs. 800 / week", "800",
                "Available", "Kathmandu", "Waterproof boots", "42", "Good",
                "Test Seller", "gear-owner");

        GearRepository.addGear(context, gear);
        assertEquals(1, GearRepository.getUserGear(context).size());
        RentalGearModel saved = GearRepository.getUserGear(context).get(0);
        assertEquals("content://test/gear-image", saved.getCustomImageUri());
        saved.setAvailability("Booked");
        assertTrue(GearRepository.updateGear(context, saved));
        assertEquals("Booked", GearRepository.getUserGear(context).get(0).getAvailability());
        assertTrue(GearRepository.deleteGear(context, saved.getId()));
        assertTrue(GearRepository.getUserGear(context).isEmpty());
    }

    private boolean hasGroup(java.util.List<ChatSummaryModel> groups, String id) {
        for (ChatSummaryModel group : groups) if (id.equals(group.getId())) return true;
        return false;
    }
}
