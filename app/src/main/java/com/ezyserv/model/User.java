package com.ezyserv.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SONI on 8/28/2017.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 12456541L;
    private String id = "";
    private String name;
    private String email;
    private String phone;
    private String company;
    private String address;
    private String isServicemen;
    private String isEmailVerified;
    private String isphoneVerified;
    private String docs1;
    private String docs2;
    private String profilepic;
    private String currentlat;
    private String currentlong;
    private String active;
    private String device_token;
    private String type;
    private String isCompany;
    private String availability;
    private String currentActive;
    private Service service;
    private String qbUserID;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(String isCompany) {
        this.isCompany = isCompany;
    }

    public String getCurrentActive() {
        return currentActive;
    }

    public void setCurrentActive(String currentActive) {
        this.currentActive = currentActive;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    //    private List<Country> country;
    private List<Location> location;

//    public void setCountry(List<Country> country) {
//        this.country = country;
//    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsServicemen() {
        return isServicemen;
    }

    public void setIsServicemen(String isServicemen) {
        this.isServicemen = isServicemen;
    }

    public String getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getIsphoneVerified() {
        return isphoneVerified;
    }

    public void setIsphoneVerified(String isphoneVerified) {
        this.isphoneVerified = isphoneVerified;
    }

    public String getDocs1() {
        return docs1;
    }

    public void setDocs1(String docs1) {
        this.docs1 = docs1;
    }

    public String getDocs2() {
        return docs2;
    }

    public void setDocs2(String docs2) {
        this.docs2 = docs2;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getCurrentlat() {
        return currentlat;
    }

    public void setCurrentlat(String currentlat) {
        this.currentlat = currentlat;
    }

    public String getCurrentlong() {
        return currentlong;
    }

    public void setCurrentlong(String currentlong) {
        this.currentlong = currentlong;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getQbUserID() {
        return qbUserID;
    }

    public void setQbUserID(String qbUserID) {
        this.qbUserID = qbUserID;
    }

    public class Service implements Serializable {
        private static final long serialVersionUID = 124598456541L;
        private List<Services> secondary;
        private List<Services> primary;

        public List<Services> getSecondary() {
            return secondary;
        }

        public void setSecondary(List<Services> secondary) {
            this.secondary = secondary;
        }

        public List<Services> getPrimary() {
            return primary;
        }

        public void setPrimary(List<Services> primary) {
            this.primary = primary;
        }
    }
}
