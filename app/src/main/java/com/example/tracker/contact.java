package com.example.tracker;

public class contact {
    private int Photo;
    private String Name;
    private String Relation;
    private String Phone;

    public contact(int mPhoto, String mName, String mRelation, String mPhone)
    {
        Photo = mPhoto;
        Name = mName;
        Relation = mRelation;
        Phone = mPhone;
    }
    public int getPhoto()
    {
        return Photo;
    }
    public String getName()
    {
        return Name;
    }
    public String getRelation()
    {
        return Relation;
    }
    public String getPhone()
    {
        return Phone;
    }
}
