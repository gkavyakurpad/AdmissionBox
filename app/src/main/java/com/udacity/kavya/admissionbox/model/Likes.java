
package com.udacity.kavya.admissionbox.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Likes implements Parcelable {
    private String username;

    public Likes() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ClassPojo [username = " + username + "]";
    }

    protected Likes(Parcel in) {
        username = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Likes> CREATOR = new Parcelable.Creator<Likes>() {
        @Override
        public Likes createFromParcel(Parcel in) {
            return new Likes(in);
        }

        @Override
        public Likes[] newArray(int size) {
            return new Likes[size];
        }
    };
}