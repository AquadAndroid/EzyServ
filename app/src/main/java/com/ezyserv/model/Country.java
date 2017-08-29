package com.ezyserv.model;

import java.io.Serializable;

/**
 * Created by SONI on 8/28/2017.
 */

public class Country implements Serializable {
    private static final long serialVersionUID = 12451L;
    private String id;
    private String name;
    private String name_code;
    private String phone_code;
    private String english_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_code() {
        return name_code;
    }

    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

}
