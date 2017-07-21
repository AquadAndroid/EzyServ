package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 7/5/2017.
 */

public class LocationObject {

    public LocationObject(String locationlabel, String serviceLocation, String locationAction) {
        this.LocationLabel = locationlabel;
        this.ServiceLocation = serviceLocation;
        this.LocationAction= locationAction;

    }



    private String LocationLabel;
    private String ServiceLocation;

    public String getLocationLabel() {
        return LocationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        LocationLabel = locationLabel;
    }

    public String getServiceLocation() {
        return ServiceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        ServiceLocation = serviceLocation;
    }

    public String getLocationAction() {
        return LocationAction;
    }

    public void setLocationAction(String locationAction) {
        LocationAction = locationAction;
    }

    private String LocationAction;



}
