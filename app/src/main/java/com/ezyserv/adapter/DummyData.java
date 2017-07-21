package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyData {
    private static final String[] service = {"Service Name1","Service Name2","Service Name3","Service Name4","Service Name5","Service Name6","Service Name7","Service Name8","Service Name9"};

    private static final String[] action = {"Add","Add","Add","Add","Add","Add","Add","Add","Add"};

    public static List<DummyListItem> getListData(){
        List<DummyListItem> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<service.length; x++){
                DummyListItem item = new DummyListItem();
                item.setService(service[x]);
                item.setAction(action[x]);
                data.add(item);
            }
        }
        return (data);
    }
    public static DummyListItem getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummyListItem item = new DummyListItem();

        item.setService(service[rand]);
        item.setAction(action[rand]);

        return item;
    }
}
