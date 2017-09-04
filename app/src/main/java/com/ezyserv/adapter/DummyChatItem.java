package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyChatItem {
    private Integer CustomerPic;

    public Integer getCustomerPic() {
        return CustomerPic;
    }

    public void setCustomerPic(Integer customerPic) {
        CustomerPic = customerPic;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    private String CustomerName;
    private String LastDate;
    private String LastMessage;
    private String Count;


}
