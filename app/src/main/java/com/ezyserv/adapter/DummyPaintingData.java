package com.ezyserv.adapter;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */





public class DummyPaintingData {
    private static final String[] ServicemanName = {"Serviceman Name1","Serviceman Name2","Serviceman Name3","Serviceman Name4","Serviceman Name5","Serviceman Name6","Serviceman Name7"};
    private static final Integer[] ServicemanImage = {R.drawable.paintimg,R.drawable.paintimg,R.drawable.paintimg,R.drawable.paintimg,R.drawable.paintimg,R.drawable.paintimg,R.drawable.paintimg};

    private static final String[] ServiceAddress = {"127 Trywhitt Rd, Singapore 207551","127 Trywhitt Rd, Singapore 207551","127 Trywhitt Rd, Singapore 207551",
            "127 Trywhitt Rd, Singapore 207551","127 Trywhitt Rd, Singapore 207551","127 Trywhitt Rd, Singapore 207551","127 Trywhitt Rd, Singapore 207551"};
    private static final String[] ServicesProvided = {"Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name",
            "Wall Painting | texture painting | waterproofing | Interior Works |Service Name"};
    private static final String[] ServiceRating = {"4.5","4.5","4.5","4.5","4.5","4.5","4.5"};
    private static final String[] AvailabilityMode = {"Available","Busy","Available","Busy","Available","Available","Available"};







    public static List<DummyPaintingItem> getListData() {
        List<DummyPaintingItem> data = new ArrayList<>();


            for (int x = 0; x < ServicemanName.length; x++) {
                DummyPaintingItem item = new DummyPaintingItem();
                item.setServicemanName(ServicemanName[x]);
                item.setServicemanImage(ServicemanImage[x]);
                item.setServiceAddress(ServiceAddress[x]);
                item.setServicesProvided(ServicesProvided[x]);
                item.setServiceRating(ServiceRating[x]);
                item.setAvailabilityMode(AvailabilityMode[x]);


                data.add(item);
            }

        return (data);
    }

    public static DummyPaintingItem getRandomListItem() {
        int rand = new Random().nextInt(5);

        DummyPaintingItem item = new DummyPaintingItem();

        item.setServicemanName(ServicemanName[rand]);
        item.setServicemanImage(ServicemanImage[rand]);
        item.setServiceAddress(ServiceAddress[rand]);
        item.setServicesProvided(ServicesProvided[rand]);
        item.setServiceRating(ServiceRating[rand]);
        item.setAvailabilityMode(AvailabilityMode[rand]);


        return item;
    }
}
