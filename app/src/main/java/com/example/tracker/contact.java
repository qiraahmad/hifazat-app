package com.example.tracker;

import java.util.Objects;

public class contact {
    private String Name;
    private String Relation;
    private String Phone;

    public contact(String mName, String mRelation, String mPhone)
    {
        Name = mName;
        Relation = mRelation;
        Phone = mPhone;
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
    public String getInitials()
    {
        String [] temp;
        String initials = "";
        temp = Name.split(" ");
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
        return Objects.equals(Name, contact.Name) &&
                Objects.equals(Relation, contact.Relation) &&
                Objects.equals(Phone, contact.Phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Relation, Phone);
    }
}