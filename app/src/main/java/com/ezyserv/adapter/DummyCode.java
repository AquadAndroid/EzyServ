package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyCode {
    private static final String[] offer_name = {"NEWUSER","CODE111","OFFER350","BUMPEROFF","OFFER999","OFFER1350",
            "OFFER1111","OFFER00","ONEONONE"};

    private static final String[] offer_discp = {"Use NEWUSER and get cashback on convenience fee upto S$50",
            "Use NEWUSER and get cashback on convenience fee upto S$50","Use NEWUSER and get cashback on convenience fee upto S$50",
            "Use NEWUSER and get cashback on convenience fee upto S$50","Use NEWUSER and get cashback on convenience fee upto S$50",
            "Use NEWUSER and get cashback on convenience fee upto S$50","Use NEWUSER and get cashback on convenience fee upto S$50",
            "Use NEWUSER and get cashback on convenience fee upto S$50","Use NEWUSER and get cashback on convenience fee upto S$50"};

    public static List<DummyPrmoList> getListData(){
        List<DummyPrmoList> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<offer_name.length; x++){
                DummyPrmoList item = new DummyPrmoList();
                item.setOFFERNAME(offer_name[x]);
                item.setOFFERDISCP(offer_discp[x]);
                data.add(item);
            }
        }
        return (data);
    }
    public static DummyPrmoList getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummyPrmoList item = new DummyPrmoList();

        item.setOFFERNAME(offer_name[rand]);
        item.setOFFERDISCP(offer_discp[rand]);

        return item;
    }
}
