package com.udacity.kavya.admissionbox.databse;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 310243577 on 1/23/2017.
 */

public class Preference {

    private static final String DEBUF_TAG ="AppPreference";
    private SharedPreferences appSharedPrefs;
    private Context mContext;

    private static final String AGE = "Age";


    private static Preference sInstance;

    private Preference(Context context) {
        this.mContext = context.getApplicationContext();
        this.appSharedPrefs = mContext.getSharedPreferences(
                "USER_DETAILS", Activity.MODE_PRIVATE);

    }

    public static synchronized Preference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Preference(context);
        }
        return sInstance;
    }

    public String getUserName() {
        return appSharedPrefs.getString("username","");
    }

    public void setuserName(String userName) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("username", userName).commit();
    }

    public String getUserEmail() {
        return appSharedPrefs.getString("useremail","");
    }

    public void setuserEmail(String userEmail) {
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("useremail", userEmail).commit();
    }




}
