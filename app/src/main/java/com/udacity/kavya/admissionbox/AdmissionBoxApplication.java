package com.udacity.kavya.admissionbox;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


public class AdmissionBoxApplication extends Application {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
        FirebaseApp.initializeApp(context);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
