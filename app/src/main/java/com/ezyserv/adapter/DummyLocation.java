package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyLocation {
    private static final String[] service = {"Location1","Location2","Location3","Location4","Location5","Location6","Location7","Location8","Location9"};

    private static final String[] action = {"ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION","ADD LOCATION"};
    private static final String[] locationno = {"AddArea & Location 1","AddArea & Location 2","AddArea & Location 3","AddArea & Location 4","AddArea & Location 5","AddArea & Location 6","AddArea & Location 7","AddArea & Location 8","AddArea & Location 9"};
    public static List<DummyLocList> getListData(){
        List<DummyLocList> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<service.length; x++){
                DummyLocList item = new DummyLocList();
                item.setLocation(service[x]);
                item.setAction(action[x]);
                item.setLocLabel(locationno[x]);
                data.add(item);
            }
        }
        return (data);
    }
    public static DummyLocList getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummyLocList item = new DummyLocList();

        item.setLocation(service[rand]);
        item.setAction(action[rand]);
        item.setLocLabel(locationno[rand]);

        return item;
    }
}
