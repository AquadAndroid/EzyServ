package com.ezyserv.adapter;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Abhishek on 29-03-2017.
 */

public class DummyChatData {
    private static final String[] CustomerName = {"John Clark","Mark Clark","Lebron James"};
    private static final Integer[] CustomerPic = {R.drawable.markzuckerberg,R.drawable.markzuckerberg,R.drawable.markzuckerberg};
    private static final String[] LastMessage = {"Address","thank you","bye"};
    private static final String[] Count = {"+1","+2","+4"};

    private static final String[] LastDate = {"21:34","18:30","20:14"};

    public static List<DummyChatItem> getListData(){
        List<DummyChatItem> data = new ArrayList<>();

        for (int i=0;i< 3;i++){
            for (int x =0; x<CustomerName.length; x++){
                DummyChatItem item = new DummyChatItem();
                item.setCustomerName(CustomerName[x]);
                item.setCustomerPic(CustomerPic[x]);
                item.setLastMessage(LastMessage[x]);
                item.setCount(Count[x]);
                item.setLastDate(LastDate[x]);



                data.add(item);
            }
        }
        return (data);
    }
  /*  public static DummyChatItem getRandomListItem(){
        int rand = new Random().nextInt(3);

        DummyChatItem item = new DummyChatItem();
        item.setCustomerName(CustomerName[rand]);
        item.setCustomerPic(CustomerPic[rand]);
        item.setLastMessage(LastMessage[rand]);
        item.setCount(Count[rand]);
        item.setLastDate(LastDate[rand]);


        return item;
    }*/
}
