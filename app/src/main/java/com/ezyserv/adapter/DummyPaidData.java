package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyPaidData {
    private static final String[] ServiceName = {"Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair","Paid For Service electrical Repair"};

    private static final String[] ServiceManName = {"Serviceman Name","Serviceman Name","Serviceman Name","Serviceman Name","Serviceman Name"};
    private static final String[] OrderId = {"order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 ","order ID #ES170623-234 "};
    private static final String[] PaidAmount = {"-SGD(S$)200","-SGD(S$)200","-SGD(S$)200","-SGD(S$)200","-SGD(S$)200"};
    private static final String[] PaidDate = {"21 July 2017","21 July 2017","21 July 2017","21 July 2017","21 July 2017"};

    public static List<DummyPaidItem> getListData(){
        List<DummyPaidItem> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<ServiceName.length; x++){
                DummyPaidItem item = new DummyPaidItem();
                item.setServiceName(ServiceName[x]);
                item.setServiceMan(ServiceManName[x]);
                item.setOrderId(OrderId[x]);
                item.setPaidAmount(PaidAmount[x]);
                item.setPaidDate(PaidDate[x]);


                data.add(item);
            }
        }
        return (data);
    }
    public static DummyPaidItem getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummyPaidItem item = new DummyPaidItem();
        item.setServiceName(ServiceName[rand]);
        item.setServiceMan(ServiceManName[rand]);
        item.setOrderId(OrderId[rand]);
        item.setPaidAmount(PaidAmount[rand]);
        item.setPaidDate(PaidDate[rand]);
        return item;
    }
}
