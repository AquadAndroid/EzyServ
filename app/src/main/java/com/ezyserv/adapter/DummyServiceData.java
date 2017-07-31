package com.ezyserv.adapter;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyServiceData {
    private static final String[] ServiceName = {"AC Installation", "Alarm & CCTV", "Electrical", "House Cleaning", "Laundry",
            "Painting","Cabinatery", "Carpentry"};
    private static final Integer[] ServiceIcon = {R.drawable.ac_installation, R.drawable.alarm_cctv, R.drawable.electrical_wiring,
            R.drawable.general_home_cleaning, R.drawable.laundry, R.drawable.painting,R.drawable.cabinatery,R.drawable.carpentry};


    public static List<DummyServiceItem> getListData() {
        List<DummyServiceItem> data = new ArrayList<>();


            for (int x = 0; x < ServiceName.length; x++) {
                DummyServiceItem item = new DummyServiceItem();
                item.setServiceName(ServiceName[x]);
                item.setServiceIcon(ServiceIcon[x]);


                data.add(item);
            }

        return (data);
    }

    public static DummyServiceItem getRandomListItem() {
        int rand = new Random().nextInt(5);

        DummyServiceItem item = new DummyServiceItem();
        item.setServiceName(ServiceName[rand]);
        item.setServiceIcon(ServiceIcon[rand]);

        return item;
    }
}
