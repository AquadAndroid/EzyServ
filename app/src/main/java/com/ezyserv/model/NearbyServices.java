package com.ezyserv.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SONI on 8/28/2017.
 */

public class NearbyServices implements Serializable {
    private static final long serialVersionUID = 124568521541L;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Data> getServiceprovider() {
        return serviceprovider;
    }

    public void setServiceprovider(List<Data> serviceprovider) {
        this.serviceprovider = serviceprovider;
    }

    private String Message;
    private Boolean status;
    private List<Data> serviceprovider;

    public class Data implements Serializable {
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

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getService_categories_id() {
            return service_categories_id;
        }

        public void setService_categories_id(String service_categories_id) {
            this.service_categories_id = service_categories_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getRedius() {
            return redius;
        }

        public void setRedius(String redius) {
            this.redius = redius;
        }

        private String id;
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
        private String country_id;
        private String currentlat;
        private String currentlong;
        private String active;
        private String device_token;
        private String user_id;
        private String service_id;
        private String service_categories_id;
        private String image;
        private String description;
        private String service_name;
        private String redius;

    }
}
