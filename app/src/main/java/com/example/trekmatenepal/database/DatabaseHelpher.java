package com.example.trekmatenepal.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trekmatenepal.models.UserModel;

public class DatabaseHelpher extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TrekMateDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user_profile";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "full_name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_BIO = "bio";
    private static final String COLUMN_TREK_COUNT = "trek_count";
    private static final String COLUMN_REGIONS = "preferred_regions";
    private static final String COLUMN_IMAGE = "image_path";

    public DatabaseHelpher(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_AGE + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_BIO + " TEXT,"
                + COLUMN_TREK_COUNT + " INTEGER,"
                + COLUMN_REGIONS + " TEXT,"
                + COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void saveUserProfile(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, 1); // Single user profile
        values.put(COLUMN_NAME, user.getFullName());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_DOB, user.getDob());
        values.put(COLUMN_AGE, user.getAge());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_LOCATION, user.getLocation());
        values.put(COLUMN_BIO, user.getBio());
        values.put(COLUMN_TREK_COUNT, user.getTrekCount());
        values.put(COLUMN_REGIONS, user.getPreferredRegions());
        values.put(COLUMN_IMAGE, user.getImagePath());

        db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @SuppressLint("Range")
    public UserModel getUserProfile() {
        UserModel user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_ID + "=?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new UserModel();
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            user.setDob(cursor.getString(cursor.getColumnIndex(COLUMN_DOB)));
            user.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_AGE)));
            user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
            user.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
            user.setBio(cursor.getString(cursor.getColumnIndex(COLUMN_BIO)));
            user.setTrekCount(cursor.getInt(cursor.getColumnIndex(COLUMN_TREK_COUNT)));
            user.setPreferredRegions(cursor.getString(cursor.getColumnIndex(COLUMN_REGIONS)));
            user.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            cursor.close();
        }
        db.close();
        return user;
    }
}
