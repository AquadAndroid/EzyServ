package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyReviewItem {
    public Integer getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(Integer profileImage) {
        ProfileImage = profileImage;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerRating() {
        return CustomerRating;
    }

    public void setCustomerRating(String customerRating) {
        CustomerRating = customerRating;
    }

    public String getCustomerReview() {
        return CustomerReview;
    }

    public void setCustomerReview(String customerReview) {
        CustomerReview = customerReview;
    }

    public Integer getServiceImage() {
        return ServiceImage;
    }

    public void setServiceImage(Integer serviceImage) {
        ServiceImage = serviceImage;
    }

    private Integer ProfileImage;
    private String CustomerName;
    private String CustomerRating;
    private String CustomerReview;
    private Integer ServiceImage;
}
