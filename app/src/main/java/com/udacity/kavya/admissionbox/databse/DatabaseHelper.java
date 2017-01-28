package com.udacity.kavya.admissionbox.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "ADDMISSIONBOX";
    static final int DATABASE_VERSION = 1;

    public static final String orgId = "orgId";
    public static final String desc = "desc";
    public static final String location = "location";
    public static final String name = "name";
    public static final String photoUrl = "photo_url";
    public static final String shortDesc = "short_desc";

    public static final String userName = "user_name";

    public static final String comments = "comments";
    public static final String email = "email";
    public static final String timeStamp = "time_stamp";


    static final String ORGANISATIONLIST_TABLE = "ORGANISATIONLIST";


    static final String CREATE_ORGANISATIONLIST_TABLE = " CREATE TABLE " + ORGANISATIONLIST_TABLE +
            " (orgId INTEGER PRIMARY KEY, " +
            " desc TEXT NOT NULL, " +
            " location TEXT NOT NULL, " +
            " name TEXT NOT NULL, " +
            " photo_url TEXT NOT NULL, " +
            " user_name TEXT NOT NULL, " +
            " email TEXT NOT NULL, " +
            " time_stamp REAL NOT NULL);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ORGANISATIONLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_ORGANISATIONLIST_TABLE);
        onCreate(db);
    }


}
