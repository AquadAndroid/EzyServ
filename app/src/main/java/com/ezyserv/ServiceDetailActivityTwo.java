package com.ezyserv;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.DummyLocation;
import com.ezyserv.adapter.ServiceLocationAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;
import com.ezyserv.utills.AppConstant;
import com.ezyserv.utills.LocationProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ServiceDetailActivityTwo extends CustomActivity implements CustomActivity.ResponseCallback,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationProvider.LocationCallback, LocationProvider.PermissionCallback {
    private Toolbar Dtoolbar;
    private TextView tvDomestic, tvConstruction, tvEvents, txt_add_location;
    private TextView tv_domestic_label, txt_mycare_counts, tv_events_label;
    private Button Continue;
    private ArrayList listdata;
    private ScrollView scrollView;
    private List<Services> allServices = null;
    private TextView txt_current_address, tv_primary;
    private boolean isCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCompany = getIntent().getBooleanExtra("isCompany", false);
        locationProvider = new LocationProvider(this, this, this);
        setResponseListener(this);
        setContentView(R.layout.activity_service_detail_two);
        Dtoolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = Dtoolbar.findViewById(R.id.detail_toolbar_title);
        TextView mCount = Dtoolbar.findViewById(R.id.detail_toolbar_count);
        mTitle.setText("Add your details");
        mCount.setText("2/2");
        actionBar.setTitle("");

        setupUiElement();
        tv_domestic_label.setText("0 services added");
        getAllServices();
        if (!MyApp.isLocationEnabled(getContext())) {
            enableGPS();
        }
    }

    private String primaryServiceId = "";

    @Override
    protected void onResume() {
        super.onResume();
        int domesticCounter = SingleInstance.getInstance().getDomesticCount();
        int myCareCounter = SingleInstance.getInstance().getMyCareCount();
        int eventsCounter = SingleInstance.getInstance().getEventsCount();
// code push test
        tv_domestic_label.setText(domesticCounter + " services added");
        txt_mycare_counts.setText(myCareCounter + " services added");
        tv_events_label.setText(eventsCounter + " services added");

        if (!SingleInstance.getInstance().getPrimaryName().isEmpty()) {
            if (!SingleInstance.getInstance().getPrimaryName().split("@")[0].equals("Primary Service")) {
                tv_primary.setText(SingleInstance.getInstance().getPrimaryName().split("@")[0]);
                primaryServiceId = SingleInstance.getInstance().getPrimaryName().split("@")[1];
            } else {
                tv_primary.setText("Primary Service");
            }
        }


    }

    private void getAllServices() {
        getCall(getContext(), AppConstant.BASE_URL + "getAllServices", "Loading services...", 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationProvider.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationProvider.disconnect();
    }

    private LocationProvider locationProvider;

    private Context getContext() {
        return ServiceDetailActivityTwo.this;
    }

    private void setupUiElement() {

        setTouchNClick(R.id.tv_domestic);
        setTouchNClick(R.id.tv_construction);
        setTouchNClick(R.id.tv_events);
        setTouchNClick(R.id.btn_continue);
        setTouchNClick(R.id.tv_primary);

        txt_current_address = findViewById(R.id.txt_current_address);
        tv_primary = findViewById(R.id.tv_primary);
        tvDomestic = findViewById(R.id.tv_domestic);
        tvConstruction = findViewById(R.id.tv_construction);
        tvEvents = findViewById(R.id.tv_events);
        txt_add_location = findViewById(R.id.txt_add_location);
        setTouchNClick(R.id.txt_add_location);

        tv_domestic_label = findViewById(R.id.tv_domestic_label);
        txt_mycare_counts = findViewById(R.id.txt_mycare_counts);
        tv_events_label = findViewById(R.id.tv_events_label);
        listdata = (ArrayList) DummyLocation.getListData();
//        listLocations = (RecyclerView) findViewById(R.id.location_list);

//        listLocations.setLayoutManager(new LinearLayoutManager(this));

//        adapter = new ServiceLocationAdapter(listdata, this);
//        listLocations.setAdapter(adapter);

        Continue = findViewById(R.id.btn_continue);
        scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_domestic) {
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 0) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key", "domestic");
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(0));
            startActivity(intent);
        } else if (v.getId() == R.id.tv_primary) {
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 0) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key", "Primary");
            intent.putExtra("isPrimary", true);
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(0));
            startActivity(intent);
        } else if (v.getId() == R.id.tv_construction) {
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 1) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key", "construction");
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(1));
            startActivity(intent);
        } else if (v.getId() == R.id.tv_events) {
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 2) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key", "events");
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(2));
            startActivity(intent);
        } else if (v.getId() == R.id.btn_continue) {
            updateProfileInformation();
        } else if (v.getId() == R.id.txt_add_location) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
            startActivityForResult(intent, 122);
        }

    }

    private void navigateAfterUpdate() {
        MyApp.setStatus(AppConstant.IS_LOGIN, true);
        startActivity(new Intent(ServiceDetailActivityTwo.this, MainActivity.class));
        finishAffinity();
    }


    private void updateProfileInformation() {
        String ids = SingleInstance.getInstance().getServicesId();
        if (ids.contains(","))
            ids = ids.substring(0, ids.length() - 1);
        SingleInstance.getInstance().setServicesId("");

        MyApp.spinnerStart(getContext(), "Taking you to home...");
        MyApp.setSharedPrefString(AppConstant.PRIMARY_SERVICE_ID, primaryServiceId);
        MyApp.setSharedPrefString(AppConstant.SECONDARY_SERVICES, ids);
        MyApp.setStatus(AppConstant.IS_SERVICES_UPDATE, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateAfterUpdate();
            }
        }, 1000);
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optString("status").equals("true")) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Services>>() {
                }.getType();
                try {
                    allServices = gson.fromJson(o.getJSONArray("data").toString(), listType);
                } catch (JSONException e) {
                    MyApp.showMassage(getContext(), "Data parsing error.");
                    e.printStackTrace();
                }
            } else {
                MyApp.showMassage(getContext(), o.optString("Message"));
            }
        } else if (callNumber == 2) {
            if (o.optString("status").equals("true")) {
                navigateAfterUpdate();
            } else {
                MyApp.showMassage(getContext(), "Data parsing error.");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Place place;
        switch (requestCode) {
            case 122:
                if (resultCode == -1) {
                    place = SingleInstance.getInstance().getSelectedPlace();
                    txt_current_address.setText(place.getAddress().toString().replace("\n", " "));
                    return;
                } else if (resultCode != 2) {
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());


    }

    @Override
    public void handleNewLocation(Location location) {
        if (!isAddressSet) {
            getCompleteAddressString(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void handleManualPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1010);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isAddressSet) {
            getCompleteAddressString(location.getLatitude(), location.getLongitude());
        }

    }

    public void enableGPS() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addOnConnectionFailedListener(
                            new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(
                                        ConnectionResult connectionResult) {

                                    MyApp.showMassage(
                                            ServiceDetailActivityTwo.this,
                                            "Location error "
                                                    + connectionResult
                                                    .getErrorCode());
                                }
                            }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(ServiceDetailActivityTwo.this,
                                        44);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });
        } else {
            LocationRequest locationRequest = LocationRequest.create();
            // locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {

                                status.startResolutionForResult(ServiceDetailActivityTwo.this,
                                        44);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });
        }
    }

    private GoogleApiClient googleApiClient;
    private boolean isAddressSet = false;

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                txt_current_address.setText(strReturnedAddress.toString().replace("\n", " "));
                isAddressSet = true;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Cannot get Address!");
        }
        return strAdd;
    }
}
