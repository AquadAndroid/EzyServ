package com.ezyserv.adapter;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyGallaryData {
    private static final Integer[] ServiceImage = {R.drawable.markzuckerberg,R.drawable.markzuckerberg,R.drawable.markzuckerberg,R.drawable.markzuckerberg,R.drawable.markzuckerberg};


    public static List<DummyGallaryItem> getListData() {
        List<DummyGallaryItem> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

                DummyGallaryItem item = new DummyGallaryItem();
                item.setServiceImage(ServiceImage[i]);



                data.add(item);

        }
        return (data);
    }

    public static DummyGallaryItem getRandomListItem() {
        int rand = new Random().nextInt(5);

        DummyGallaryItem item = new DummyGallaryItem();

        item.setServiceImage(ServiceImage[rand]);


        return item;
    }
}
