package com.ezyserv.application;

import com.ezyserv.model.Services;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aquad on 21-07-2017.
 */

public class SingleInstance {
    private static final SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }

    private SingleInstance() {
    }

    private Place selectedPlace;

    public Place getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
    }

    private Services selectedServiceCategory;

    public Services getSelectedServiceCategory() {
        return selectedServiceCategory;
    }

    public void setSelectedServiceCategory(Services selectedServiceCategory) {
        this.selectedServiceCategory = selectedServiceCategory;
    }
}
