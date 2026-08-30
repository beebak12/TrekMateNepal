# Implementation Plan - Fully Functional Edit Profile with Gallery & Database

Implement the Edit Profile screen with a precise overlapping UI, image selection from gallery with permissions, and full database persistence using SQLite.

## User Review Required

> [!IMPORTANT]
> The current icons in the project (`ic_person`, `ic_email`, etc.) are currently identical placeholders. I will replace them with specific Material Design icons that match your design.
>
> [!NOTE]
> I will use the modern `ActivityResultLauncher` for image picking and permission requests, which is the recommended approach for Android 13+.

## Proposed Changes

### 1. Resource & UI Layer

#### [MODIFY] [activity_edit_profile.xml](file:///D:/Program Files/Android Studio/StudioProjects/TrekMateNepal/app/src/main/res/layout/activity_edit_profile.xml)
- Ensure the profile image overlaps the background header correctly.
- Update `ImageView` and `EditText` icons to their specific intended versions.
- Add `android:imeOptions="actionNext"` and `android:inputType` to all fields for better UX.

#### [MODIFY] Regenerate Specific Vector Icons
- Replace the current generic Android icons with:
    - `ic_person.xml` (User icon)
    - `ic_email.xml` (Mail icon)
    - `ic_phone.xml` (Phone icon)
    - `ic_calendar.xml` (Calendar icon)
    - `ic_location.xml` (Pin icon)
    - `ic_bio.xml` (Bio/Text icon)
    - `ic_camera.xml` (Camera icon)

### 2. Data & Database Layer

#### [MODIFY] [UserModel.java](file:///D:/Program Files/Android Studio/StudioProjects/TrekMateNepal/app/src/main/java/com/example/trekmatenepal/models/UserModel.java)
- Add fields: `fullName`, `username`, `email`, `phone`, `dob`, `age`, `gender`, `location`, `bio`, `trekCount`, `preferredRegions`, `imageUri`.
- Add constructor, getters, and setters.

#### [MODIFY] [DatabaseHelpher.java](file:///D:/Program Files/Android Studio/StudioProjects/TrekMateNepal/app/src/main/java/com/example/trekmatenepal/database/DatabaseHelpher.java)
- Implement `SQLiteOpenHelper`.
- Create `user_profile` table.
- Implement `saveUserProfile(UserModel user)` and `getUserProfile()`.

### 3. Logic Layer (EditProfileActivity.java)

#### [MODIFY] [EditProfileActivity.java](file:///D:/Program Files/Android Studio/StudioProjects/TrekMateNepal/app/src/main/java/com/example/trekmatenepal/activities/EditProfileActivity.java)
- **Permission Handling**: Check and request `READ_MEDIA_IMAGES` (API 33+) or `READ_EXTERNAL_STORAGE` (<33).
- **Image Picker**: Use `ActivityResultLauncher<PickVisualMediaRequest>` to allow users to select an image from their gallery.
- **Date Picker**: Implement `DatePickerDialog` to select DOB and calculate age automatically.
- **Form Persistence**: On "Save Changes", collect all data into `UserModel` and save to SQLite.
- **Data Loading**: In `onCreate`, load existing data from SQLite and populate the fields.

### 4. Integration (ProfileActivity.java)

#### [MODIFY] [ProfileActivity.java](file:///D:/Program Files/Android Studio/StudioProjects/TrekMateNepal/app/src/main/java/com/example/trekmatenepal/activities/ProfileActivity.java)
- Update `onResume` to reload user data from the database so changes are visible immediately after saving.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure no build errors.

### Manual Verification
1.  **Image Edit**: Tap the camera icon, allow permission, select an image, and verify it updates the profile picture.
2.  **Date selection**: Select a date and verify the "Age" text updates correctly.
3.  **Database Save**: Fill out all fields, click "Save Changes", and verify a success Toast appears.
4.  **Profile Refresh**: Navigate back to the Profile screen and verify all updated info (including image) is displayed.
5.  **Persistence**: Close and reopen the app to ensure data persists.
