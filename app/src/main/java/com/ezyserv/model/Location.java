package com.ezyserv.model;

import java.io.Serializable;

/**
 * Created by SONI on 8/28/2017.
 */

public class Location implements Serializable {
    private static final long serialVersionUID = 12478902315L;
    private String id;
    private String user_id;
    private String latitude;
    private String longitude;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
