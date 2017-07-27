package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyActiveData {
    private static final String[] AddedLabel = {"Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair"};

    private static final String[] BankName = {"Serviceman Name","Serviceman Name","Serviceman Name","Serviceman Name","Serviceman Name"};
    private static final String[] Via = {"order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 "};
    private static final String[] AddedAmount = {"-SGD(S$)200","-SGD(S$)200","-SGD(S$)200","-SGD(S$)200","-SGD(S$)200"};
    private static final String[] AddedDate = {"21 July 2017","21 July 2017","21 July 2017","21 July 2017","21 July 2017"};

    public static List<DummyAddedItem> getListData(){
        List<DummyAddedItem> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<AddedLabel.length; x++){
                DummyAddedItem item = new DummyAddedItem();
                item.setAddedLabel(AddedLabel[x]);
                item.setBankName(BankName[x]);
                item.setVia(Via[x]);
                item.setAddedAmount(AddedAmount[x]);
                item.setAddedDate(AddedDate[x]);


                data.add(item);
            }
        }
        return (data);
    }
    public static DummyAddedItem getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummyAddedItem item = new DummyAddedItem();

        item.setAddedLabel(AddedLabel[rand]);
        item.setBankName(BankName[rand]);
        item.setVia(Via[rand]);
        item.setAddedAmount(AddedAmount[rand]);
        item.setAddedDate(AddedDate[rand]);

        return item;
    }
}
