# 🧪 TREK PARTNER FINDER — TESTING GUIDE

## Quick Test Flow

### 1️⃣ Launch App
```
Splash Screen (3s) → Start Screen → Login/Signup → Dashboard
```

### 2️⃣ Navigate to Partner Finder
**Option A:** Tap "Find Trek Partners" card on Dashboard  
**Option B:** Tap "Partners" in Bottom Navigation

**Expected:** Opens `PartnerFinderActivity`

---

## 3️⃣ Filter Screen Testing

### Destination Chips
- [ ] Tap "Everest Base Camp" chip → highlights in purple
- [ ] Tap other chips → updates active chip style
- [ ] Verify chip text: "All", "Everest Base Camp", "Annapurna Base Camp", "Langtang Valley", "Mardi Himal", "Manaslu Circuit"

### Filter Dialogs
- [ ] Tap "Destination" row → shows dialog with 6 options
- [ ] Tap "Trek Date" row → shows dialog with 6 date options
- [ ] Tap "Duration" row → shows dialog with 5 duration options
- [ ] Tap "Gender" row → shows dialog with 4 gender options
- [ ] Tap "Group Type" row → shows dialog with 4 group options
- [ ] Select any option → filter row updates with selection

### Search Bar
- [ ] Type partner name ("Sujan") → stays in input field
- [ ] Type destination ("Everest") → stays in input field
- [ ] Clear search → works as expected

### Apply Filters Button
- [ ] Tap "Apply Filters" → opens `PartnerListActivity`
- [ ] Passed filters should be applied to list
- [ ] Back button returns to `PartnerFinderActivity`

---

## 4️⃣ Partner List Screen Testing

### RecyclerView Display
- [ ] 8 partner cards visible in grid layout
- [ ] Each card shows:
  - [ ] Partner photo (R.drawable.partner1/2/3/4)
  - [ ] Name ("Sujan Karki", "Anita Gurung", etc.)
  - [ ] Location ("Pokhara, Nepal", etc.)
  - [ ] Destination ("Everest Base Camp", etc.)
  - [ ] Dates ("20 Apr – 2 May", etc.)
  - [ ] Duration ("12 Days", etc.)
  - [ ] Online status indicator (🟢 green for online, hidden for offline)

### Partner Cards
- [ ] Tap any partner card → opens `PartnerProfileActivity`
- [ ] Tap "View Profile" button → opens `PartnerProfileActivity`
- [ ] Verify correct partner data is passed

### Empty State
- [ ] If filtered results are empty, shows "No partners found" message
- [ ] RecyclerView hidden when empty

### Toolbar
- [ ] Tap back button (←) → returns to `PartnerFinderActivity`
- [ ] Toolbar shows "Partners Found: 8" (or filtered count)

---

## 5️⃣ Partner Profile Screen Testing

### Profile Header
- [ ] Cover image displays (R.drawable.everest, annapurna, etc.)
- [ ] Partner photo visible with rounded corners
- [ ] Online status badge shows (🟢 Online or ⚫ Offline)

### Profile Info
- [ ] Name displayed correctly
- [ ] Location shown ("Pokhara, Nepal")
- [ ] Rating visible ("4.8⭐")
- [ ] Review count shown ("24 reviews")

### Stats Row
- [ ] Age shown ("28 Years")
- [ ] Treks count ("8+")
- [ ] Rating ("4.8⭐")
- [ ] Partners count ("15")

### About Section
- [ ] About text displays (e.g., "Adventure lover and nature enthusiast...")
- [ ] Text wraps properly on all screen sizes

### Trek Plan
- [ ] Destination, dates, duration shown
- [ ] Example: "Everest Base Camp • 20 Apr – 2 May • 12 Days"

### Interests
- [ ] Interest tags display in horizontal scroll
- [ ] Tags: "Photography", "Camping", "Nature", "Adventure", etc.
- [ ] Chips styled with purple background

### Action Buttons
- [ ] "Send Trek Request" button visible
- [ ] "Message" button visible
- [ ] Both buttons styled with purple theme

### Navigation
- [ ] Tap "Send Trek Request" → opens `SendTrekRequestActivity`
- [ ] Tap "Message" → opens `ChatActivity`
- [ ] Back button returns to `PartnerListActivity`

---

## 6️⃣ Send Trek Request Screen Testing

### Request Form
- [ ] Message template pre-filled: "Hi [Partner], I'd love to trek together..."
- [ ] Text area editable
- [ ] Can clear and type custom message

### My Details Display
- [ ] "My Details" section shows:
  - [ ] Your name
  - [ ] Your age
  - [ ] Your gender
  - [ ] Your location
  - [ ] Your experience level

### Send Button
- [ ] "Send Request" button visible and clickable
- [ ] Button disabled if message is empty (optional validation)
- [ ] Tap send → opens `RequestSentActivity`

### Navigation
- [ ] Back button returns to `PartnerProfileActivity`

---

## 7️⃣ Request Sent (Success) Screen Testing

### Success Message
- [ ] Confirmation text displayed: "Request sent successfully!"
- [ ] Partner name shown
- [ ] Trek details displayed:
  - [ ] Destination
  - [ ] Dates
  - [ ] Duration

### Action Buttons
- [ ] "Go to My Requests" button visible
  - [ ] Tap → opens `MyRequestsActivity`
- [ ] Back button (if present) returns to previous screen

---

## 8️⃣ My Requests Screen Testing

### Request Inbox
- [ ] At least 2 sample requests displayed:
  - [ ] Sujan Karki - Status: 🟡 Pending
  - [ ] Anita Gurung - Status: 🟢 Accepted

### Request Cards
Each card displays:
- [ ] Partner photo
- [ ] Partner name
- [ ] Destination
- [ ] Trek dates + duration
- [ ] Status badge (Pending/Accepted/Declined)
- [ ] "Chat Now" button

### Status Badges
- [ ] 🟡 Pending - Orange color
- [ ] 🟢 Accepted - Green color
- [ ] 🔴 Declined - Red color

### Chat Button
- [ ] Tap "Chat Now" → opens `ChatActivity` with correct partner
- [ ] Partner name, image, and online status passed correctly

### Empty State
- [ ] If no requests, shows "No requests yet" message
- [ ] Encourages user to send trek requests

### Navigation
- [ ] Back button returns to previous screen

---

## 9️⃣ Chat Screen Testing

### Chat Header
- [ ] Partner photo displayed (circular)
- [ ] Partner name shown
- [ ] Online status: "🟢 Online" (green) or "⚫ Offline" (gray)

### Message List
- [ ] Pre-loaded sample conversation displayed:
  1. "Hi Bibek! 👋 I received your request." - Received (left, white)
  2. "Hi Sujan! 😊 Great to hear that." - Sent (right, purple)
  3. "Yes, we can plan together!" - Received
  4. "Are you available for a quick call?" - Received
  5. "Sure, I'm available now." - Sent
  6. "Great! Let's connect. 🏔️" - Received

### Message Bubbles
- **Sent messages:**
  - [ ] Appear on right side
  - [ ] Purple/dark background
  - [ ] White text
  - [ ] Time displayed below

- **Received messages:**
  - [ ] Appear on left side
  - [ ] White/light background with border
  - [ ] Dark text
  - [ ] Time displayed below

### Message Timestamps
- [ ] All messages show time (e.g., "10:30 AM")
- [ ] Times are readable and properly formatted

### Message Input
- [ ] EditText for typing message
- [ ] Placeholder text: "Type a message..."
- [ ] Editable and focused on open

### Send Button
- [ ] Send icon (📤) visible and clickable
- [ ] Tap → message appears in list on right side
- [ ] Input field cleared after send
- [ ] List auto-scrolls to newest message
- [ ] Time stamp added automatically

### Attachment & Camera Buttons
- [ ] Camera button (📷) shows toast: "Camera coming soon"
- [ ] Attachment button (📎) shows toast: "File attachment coming soon"

### Navigation
- [ ] Back button (←) returns to previous screen
- [ ] Back stack maintained properly

---

## 🔄 End-to-End Flow Test

### Complete User Journey
1. ✅ Dashboard → "Find Trek Partners"
2. ✅ PartnerFinderActivity (apply filters)
3. ✅ PartnerListActivity (view 8 partners)
4. ✅ Tap partner card
5. ✅ PartnerProfileActivity (view full profile)
6. ✅ Tap "Send Trek Request"
7. ✅ SendTrekRequestActivity (compose message)
8. ✅ RequestSentActivity (success)
9. ✅ MyRequestsActivity (inbox)
10. ✅ Tap "Chat Now"
11. ✅ ChatActivity (message conversation)
12. ✅ Send message → appears on right
13. ✅ Back through all screens

---

## 🐛 Known Issues & Limitations

### Current (Local Implementation)
- ✅ Chat messages stored in-memory only (cleared on close)
- ✅ No real-time sync between chat instances
- ✅ Attachment/Camera buttons are placeholder toasts
- ✅ Partner data is hardcoded sample data

### Coming Soon (Backend Integration)
- 🔄 Socket.IO real-time chat
- 🔄 REST API for partner search
- 🔄 Database persistence for requests
- 🔄 File upload for attachments
- 🔄 Photo capture from camera

---

## 📋 Regression Test Checklist

After any code changes, verify:
- [ ] All 33 activities compile without errors
- [ ] APK builds successfully (`./gradlew assembleDebug`)
- [ ] App launches without crash
- [ ] Dashboard loads
- [ ] All 6 Partner Finder screens navigate correctly
- [ ] Sample data loads (8 partners, 2 requests)
- [ ] RecyclerView items render without issues
- [ ] Back button works on every screen
- [ ] No "Activity not exported" warnings
- [ ] No resource not found errors
- [ ] Chat messages send and display correctly

---

## 📱 Device Testing Requirements

**Minimum Configuration:**
- Android 6.0 (API 23)
- Screen sizes: 4.5" - 6.5"
- RAM: 2GB minimum

**Recommended:**
- Android 10+ (API 29+)
- Physical device for realistic testing
- 4"+ display for chat message UI

**Orientation:**
- Test Portrait mode (primary)
- Test Landscape mode (if supported)

---

## ✅ Test Sign-Off

| Component | Tested | Status | Notes |
|-----------|--------|--------|-------|
| PartnerFinderActivity | [ ] | ⏳ | Filter chips, dialogs, search |
| PartnerListActivity | [ ] | ⏳ | RecyclerView, 8 partners, navigation |
| PartnerProfileActivity | [ ] | ⏳ | Profile layout, all fields, buttons |
| SendTrekRequestActivity | [ ] | ⏳ | Form, template, send button |
| RequestSentActivity | [ ] | ⏳ | Success screen, navigation |
| MyRequestsActivity | [ ] | ⏳ | Request cards, status, chat btn |
| ChatActivity | [ ] | ⏳ | Dual bubbles, send, timestamps |
| End-to-End | [ ] | ⏳ | Complete user journey |
| Build System | [ ] | ⏳ | Gradle build, no errors |

---

## 🎯 Testing Tips

1. **Use Logcat for debugging:**
   ```bash
   adb logcat | grep TrekMate
   ```

2. **Test on multiple devices:**
   - Emulator (for quick testing)
   - Physical device (for real performance)

3. **Check memory usage:**
   - Monitor heap with Android Profiler
   - Ensure no memory leaks on activity exit

4. **Test edge cases:**
   - Very long partner names
   - Empty search results
   - Network delays (simulate with Chrome DevTools)

5. **Verify all strings:**
   - Check strings.xml for untranslated text
   - Verify no hardcoded strings in code

---

**Happy Testing! 🚀**

