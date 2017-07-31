package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyPaintingItem {
    public Integer getServicemanImage() {
        return ServicemanImage;
    }

    public void setServicemanImage(Integer servicemanImage) {
        ServicemanImage = servicemanImage;
    }

    public String getServicemanName() {
        return ServicemanName;
    }

    public void setServicemanName(String servicemanName) {
        ServicemanName = servicemanName;
    }

    public String getServiceAddress() {
        return ServiceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        ServiceAddress = serviceAddress;
    }

    public String getServicesProvided() {
        return ServicesProvided;
    }

    public void setServicesProvided(String servicesProvided) {
        ServicesProvided = servicesProvided;
    }

    public String getServiceRating() {
        return ServiceRating;
    }

    public void setServiceRating(String serviceRating) {
        ServiceRating = serviceRating;
    }

    public String getAvailabilityMode() {
        return AvailabilityMode;
    }

    public void setAvailabilityMode(String availabilityMode) {
        AvailabilityMode = availabilityMode;
    }

    private Integer ServicemanImage;
    private String ServicemanName;
    private String ServiceAddress;
    private String ServicesProvided;
    private String ServiceRating;
    private String AvailabilityMode;

}
