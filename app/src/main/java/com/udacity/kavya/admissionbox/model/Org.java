package com.udacity.kavya.admissionbox.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Org implements Parcelable {

    private String orgId;

    private String desc;

    private String location;

    private String name;

    private ArrayList<Likes> likes;

    private ArrayList<Chats> chats;

    private String shortDesc;

    private String photoUrl;

    private ArrayList<Comments> comments;

    Org() {
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "ClassPojo [orgId = " + orgId + ", desc = " + desc + ", location = " + location + ", name = " + name + ", likes = " + likes + ", shortDesc = " + shortDesc + ", photoUrl = " + photoUrl + ", comments = " + comments + "]";
    }


    protected Org(Parcel in) {
        orgId = in.readString();
        desc = in.readString();
        location = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            likes = new ArrayList<Likes>();
            in.readList(likes, Likes.class.getClassLoader());
        } else {
            likes = null;
        }
        if (in.readByte() == 0x01) {
            chats = new ArrayList<Chats>();
            in.readList(chats, Chats.class.getClassLoader());
        } else {
            chats = null;
        }
        shortDesc = in.readString();
        photoUrl = in.readString();
        if (in.readByte() == 0x01) {
            comments = new ArrayList<Comments>();
            in.readList(comments, Comments.class.getClassLoader());
        } else {
            comments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orgId);
        dest.writeString(desc);
        dest.writeString(location);
        dest.writeString(name);
        if (likes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(likes);
        }
        if (chats == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(chats);
        }
        dest.writeString(shortDesc);
        dest.writeString(photoUrl);
        if (comments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(comments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Org> CREATOR = new Parcelable.Creator<Org>() {
        @Override
        public Org createFromParcel(Parcel in) {
            return new Org(in);
        }

        @Override
        public Org[] newArray(int size) {
            return new Org[size];
        }
    };
}