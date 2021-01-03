package com.example.tracker;

public class Location {
    String lat;
    String lng;
    String user_id;

    public Location(String lat, String lng, String user_id) {
        this.lat = lat;
        this.lng = lng;
        this.user_id = user_id;
    }

    public Location()
    {

    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
