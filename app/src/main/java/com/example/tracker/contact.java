package com.example.tracker;

import java.util.Objects;

public class contact {
    private String name;
    private String relation;
    private String mobile;
    private String user_id;

    public contact()
    { }

    public contact(String mName, String mRelation, String mPhone, String mUid)
    {
        name = mName;
        relation = mRelation;
        mobile = mPhone;
        user_id = mUid;
    }
    public String getName()
    {
        return name;
    }
    public String getRelation()
    {
        return relation;
    }
    public String getMobile()
    {
        return mobile;
    }
    public String getUser_id()
    {
        return user_id;
    }
    public String getInitials()
    {
        String [] temp;
        String initials = "";
        temp = name.split(" ");
        for (String x: temp) {
            initials = initials + x.substring(0,1).toUpperCase();
        }
        return initials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        contact contact = (contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(relation, contact.relation) &&
                Objects.equals(mobile, contact.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relation, mobile);
    }
}