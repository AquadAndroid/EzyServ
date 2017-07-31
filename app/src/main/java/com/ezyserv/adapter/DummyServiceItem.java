package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyServiceItem {
    public Integer getServiceIcon() {
        return ServiceIcon;
    }

    public void setServiceIcon(Integer serviceIcon) {
        ServiceIcon = serviceIcon;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    private Integer ServiceIcon;
    private String ServiceName;

}
