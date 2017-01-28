package com.udacity.kavya.admissionbox.model;


public class Users
{
    private String email;

    private String photourl;

    private String name;

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getPhotourl ()
    {
        return photourl;
    }

    public void setPhotourl (String photourl)
    {
        this.photourl = photourl;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [email = "+email+", photourl = "+photourl+", name = "+name+"]";
    }
}
