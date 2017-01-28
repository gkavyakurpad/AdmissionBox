package com.udacity.kavya.admissionbox.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Comments implements Parcelable {
    private long timeStamp;

    private String email;

    private String comment;

    public Comments() {
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ClassPojo [timeStamp = " + timeStamp + ", email = " + email + ", comment = " + comment + "]";
    }

    protected Comments(Parcel in) {
        timeStamp = in.readLong();
        email = in.readString();
        comment = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timeStamp);
        dest.writeString(email);
        dest.writeString(comment);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Comments> CREATOR = new Parcelable.Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };
}