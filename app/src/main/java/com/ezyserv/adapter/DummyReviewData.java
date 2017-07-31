package com.ezyserv.adapter;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyReviewData {
    private static final String[] CustomerName = {"Bhavesh", "Umer", "Reddy", "Vishal", "Avinash"};
    private static final Integer[] ProfileImage = {R.drawable.markzuckerberg, R.drawable.markzuckerberg, R.drawable.markzuckerberg, R.drawable.markzuckerberg, R.drawable.markzuckerberg};
    private static final String[] CustomerRating = {"4.5", "4", "3", "4.3", "5"};
    private static final String[] CustomerReview = {"A badge on your site that helps users easily identify your site with the Google brand. The badge displays the seller rating score of 1-5 stars and can be placed on any page on your site.",
            "A badge on your site that helps users easily identify your site with the Google brand. The badge displays the seller rating score of 1-5 stars and can be placed on any page on your site.",
            "A badge on your site that helps users easily identify your site with the Google brand. The badge displays the seller rating score of 1-5 stars and can be placed on any page on your site.",
            "A badge on your site that helps users easily identify your site with the Google brand. The badge displays the seller rating score of 1-5 stars and can be placed on any page on your site.",
            "A badge on your site that helps users easily identify your site with the Google brand. The badge displays the seller rating score of 1-5 stars and can be placed on any page on your site."};
    private static final Integer[] ServiceImage = {R.drawable.paintimg, R.drawable.paintimg, R.drawable.paintimg, R.drawable.paintimg, R.drawable.paintimg};


    public static List<DummyReviewItem> getListData() {
        List<DummyReviewItem> data = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < CustomerName.length; x++) {
                DummyReviewItem item = new DummyReviewItem();
                item.setCustomerName(CustomerName[x]);
                item.setProfileImage(ProfileImage[x]);
                item.setCustomerRating(CustomerRating[x]);
                item.setCustomerReview(CustomerReview[x]);
                item.setServiceImage(ServiceImage[x]);


                data.add(item);
            }
        }
        return (data);
    }

    public static DummyReviewItem getRandomListItem() {
        int rand = new Random().nextInt(5);

        DummyReviewItem item = new DummyReviewItem();
        item.setCustomerName(CustomerName[rand]);
        item.setProfileImage(ProfileImage[rand]);
        item.setCustomerRating(CustomerRating[rand]);
        item.setCustomerReview(CustomerReview[rand]);
        item.setServiceImage(ServiceImage[rand]);
        return item;
    }
}
