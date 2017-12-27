package com.ezyserv;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String TAG = "MapsActivity", from;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    Button btnSendLocationBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        from = getIntent().getStringExtra("from");

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()

                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnSendLocationBack = findViewById(R.id.btnSendLocationBack);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (from.equals("SeeLocation"))
            btnSendLocationBack.setVisibility(View.GONE);

        btnSendLocationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocationBack();
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        if (from.equals("SeeLocation")) {
            String lat = getIntent().getStringExtra("lat");
            String lng = getIntent().getStringExtra("lng");
            Log.e(TAG, "onMapReady: " + lat + " : " + lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(lat),
                            Double.valueOf(lng)), 15));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Double.valueOf(lat),
                    Double.valueOf(lng)));
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(lat), Double.valueOf(lng))));
            mMap.addMarker(markerOptions);
        } else
            getDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLastKnownLocation.setLatitude(latLng.latitude);
                mLastKnownLocation.setLongitude(latLng.longitude);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), 15));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()));
                        mMap.clear();
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude())));
                        mMap.addMarker(markerOptions);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    void sendLocationBack() {
        Bundle extras = new Bundle();
        extras.putDouble("getLatitude", mLastKnownLocation.getLatitude());
        extras.putDouble("getLongitude", mLastKnownLocation.getLongitude());
        Intent result = getIntent();
        result.putExtras(extras);
        setResult(RESULT_OK, result);
        finish();
    }

}
