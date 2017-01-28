package com.udacity.kavya.admissionbox.databse;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.udacity.kavya.admissionbox.model.Org;

import java.util.ArrayList;
import java.util.HashMap;


public class DataAccessImpl extends ContentProvider {

    static final String PROVIDER_NAME = "com.udacity.kavya.addmissionbox";
    static final String URL = "content://" + PROVIDER_NAME + "/organisation_list";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int ORG_ALL = 1;
    static final String ORGANISATIONLIST_TABLE = "ORGANISATIONLIST";
    private static HashMap<String, String> ORGANISATIONLIST_PROJECTION_MAP;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "organizations", ORG_ALL);
    }


    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ORGANISATIONLIST_TABLE);

        switch (uriMatcher.match(uri)) {
            case ORG_ALL:
                qb.setProjectionMap(ORGANISATIONLIST_PROJECTION_MAP);
                break;


            default:
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = "orgId";
        }

        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {


        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /**
         * Add a new student record
         */
        long rowID = db.insert(ORGANISATIONLIST_TABLE, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public void insertSqlLite(ArrayList<Org> mOrgList) {

        db.beginTransaction();
        String sql = "Insert or Replace into " + ORGANISATIONLIST_TABLE +
                "(" + DatabaseHelper.orgId +
                "," + DatabaseHelper.desc
                + "," + DatabaseHelper.location
                + "," + DatabaseHelper.name
                + "," + DatabaseHelper.photoUrl
                + "," + DatabaseHelper.shortDesc
                + "," + DatabaseHelper.userName
                + "," + DatabaseHelper.comments
                + "," + DatabaseHelper.email
                + "," + DatabaseHelper.timeStamp
                + ") values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insert = db.compileStatement(sql);
        for (Org org : mOrgList) {
            insert.bindString(1, org.getOrgId());
            insert.bindString(2, org.getDesc());
            insert.bindString(3, org.getLocation());
            insert.bindString(4, org.getName());
            insert.execute();
        }

    }

}
