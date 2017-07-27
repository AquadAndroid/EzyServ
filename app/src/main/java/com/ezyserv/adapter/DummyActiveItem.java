package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyActiveItem {
    private String ServiceName;

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceMan() {
        return ServiceMan;
    }

    public void setServiceMan(String serviceMan) {
        ServiceMan = serviceMan;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getPaidDate() {
        return PaidDate;
    }

    public void setPaidDate(String paidDate) {
        PaidDate = paidDate;
    }

    private String ServiceMan;
    private String OrderId;
    private String PaidAmount;
    private String PaidDate;


}
