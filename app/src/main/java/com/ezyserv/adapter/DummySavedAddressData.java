package com.ezyserv.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummySavedAddressData {
    private static final String[] AddressLabel = {"Address 1","Address 2","Address 3","Address 4","Address 5"};

    private static final String[] AddressOne = {"Address Line 1","Address Line 1","Address Line 1","Address Line 1","Address Line 1"};
    private static final String[] AddressTwo = {"Address Line 2","Address Line 2","Address Line 2","Address Line 2","Address Line 2"};
    private static final String[] Pin = {"667854","667854","667854","667854","667854"};

    public static List<DummySavedAddressItem> getListData(){
        List<DummySavedAddressItem> data = new ArrayList<>();

        for (int i=0;i< 4;i++){
            for (int x =0; x<AddressLabel.length; x++){
                DummySavedAddressItem item = new DummySavedAddressItem();
                item.setAddressLabel(AddressLabel[x]);
                item.setAddressOne(AddressOne[x]);
                item.setAddressTwo(AddressTwo[x]);
                item.setPin(Pin[x]);

                data.add(item);
            }
        }
        return (data);
    }
    public static DummySavedAddressItem getRandomListItem(){
        int rand = new Random().nextInt(5);

        DummySavedAddressItem item = new DummySavedAddressItem();
        item.setAddressLabel(AddressLabel[rand]);
        item.setAddressOne(AddressOne[rand]);
        item.setAddressTwo(AddressTwo[rand]);
        item.setPin(Pin[rand]);

        return item;
    }
}
