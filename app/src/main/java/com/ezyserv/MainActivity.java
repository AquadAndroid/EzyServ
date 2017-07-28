package com.ezyserv;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.fragment.FragmentDrawer;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;


public class MainActivity extends CustomActivity implements FragmentDrawer.FragmentDrawerListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationProvider.LocationCallback, LocationProvider.PermissionCallback, GoogleMap.OnCameraIdleListener,
        ResultCallback<LocationSettingsResult> {

    private boolean isFirstSet = false;
    private GoogleApiClient googleApiClient;
    private LatLng sourceLocation = null;
    private LocationProvider locationProvider;
    private FragmentDrawer drawerFragment;
    private TextView txt_location;

    private GoogleMap mMap;
    private DrawerLayout drawer;
    private SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "MainActivity";
    private TextView Tv_search, Tv_service, Tv_notification, Tv_account;
    private ImageButton navBtn, btn_search;

    FloatingActionButton Show_all, Domestic, Construction, Events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {

            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Set paddingTop of toolbar to height of status bar.
            // Fixes statusbar covers toolbar issue
            RelativeLayout v = (RelativeLayout) findViewById(R.id.rl_top);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
            lp.setMargins(0, getStatusBarHeight(), 0, -getStatusBarHeight());
//            v.setPadding(getStatusBarHeight(), getStatusBarHeight(), getStatusBarHeight(), 0);
        }

        setupUiElements();
        locationProvider = new LocationProvider(this, this, this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        DrawerLayout.LayoutParams lll = (DrawerLayout.LayoutParams) drawer.getLayoutParams();

//        lll.set
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), null);
        drawerFragment.setDrawerListener(this);


        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_address = (TextView) findViewById(R.id.txt_address);
        setTouchNClick(R.id.txt_address);
        setTouchNClick(R.id.txt_location);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sourceLocation == null) {
                    locationProvider = new LocationProvider(MainActivity.this, MainActivity.this, MainActivity.this);
                    locationProvider.connect();
                }
            }
        }, (1000 * 10));

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationProvider.connect();
    }

    private boolean isLocationManual = false;
    private boolean isDialogShown = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!MyApp.isLocationEnabled(this) && !isLocationManual && !isDialogShown) {
            isDialogShown = true;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.location_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button btn_locate = (Button) dialog.findViewById(R.id.btn_locate);
            TextView txt_enter_manually = (TextView) dialog.findViewById(R.id.txt_enter_manually);
            txt_enter_manually.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDialogShown = false;
                    isLocationManual = true;
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
                    MainActivity.this.startActivityForResult(intent, 122);
                    dialog.dismiss();
                }
            });

            btn_locate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableGPS();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sourceLocation != null && isFirstSet) {
//            currentLocation.distanceTo()
            // getNearbyDrivers(sourceLocation.latitude + "", sourceLocation.longitude + "");
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
                                            MainActivity.this,
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
                                status.startResolutionForResult(MainActivity.this,
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

                                status.startResolutionForResult(MainActivity.this,
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rl_tab_1) {

            Tv_search.setSelected(true);
            Tv_service.setSelected(false);
            Tv_notification.setSelected(false);
            Tv_account.setSelected(false);

            Tv_search.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search_active, 0, 0);
            Tv_notification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications_inactive, 0, 0);
            Tv_service.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.services_inactive, 0, 0);
            Tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_inactive, 0, 0);


            Tv_search.setTextColor(Color.parseColor("#ED365B"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));
        } else if (v.getId() == R.id.rl_tab_2) {
            Tv_search.setSelected(false);
            Tv_service.setSelected(true);
            Tv_notification.setSelected(false);
            Tv_account.setSelected(false);

            Tv_search.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search_inactive, 0, 0);
            Tv_notification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications_inactive, 0, 0);
            Tv_service.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.services_active, 0, 0);
            Tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_inactive, 0, 0);

            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#ED365B"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));

        } else if (v.getId() == R.id.rl_tab_3) {
            Tv_search.setSelected(false);
            Tv_service.setSelected(false);
            Tv_notification.setSelected(true);
            Tv_account.setSelected(false);

            Tv_search.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search_inactive, 0, 0);
            Tv_notification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications_active, 0, 0);
            Tv_service.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.services_inactive, 0, 0);
            Tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_inactive, 0, 0);


            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#ED365B"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));
//            openImage();

        } else if (v.getId() == R.id.rl_tab_4) {
            Tv_search.setSelected(false);
            Tv_service.setSelected(false);
            Tv_notification.setSelected(false);
            Tv_account.setSelected(true);

            Tv_search.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search_inactive, 0, 0);
            Tv_notification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications_inactive, 0, 0);
            Tv_service.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.services_inactive, 0, 0);
            Tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0);


            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#ED365B"));
            startActivity(new Intent(MainActivity.this, AddMoneyActivity.class));

        } else if (v == navBtn) {
            drawer.openDrawer(GravityCompat.START);

        } else if (v == txt_location) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
            MainActivity.this.startActivityForResult(intent, 122);
        } else if (v == txt_address) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
            MainActivity.this.startActivityForResult(intent, 122);
        } else if (v == btn_search) {
            searchRadius();
        } else if (v == Show_all) {
            startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Domestic) {
            startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Construction) {
            startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Events) {
            startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
                    this.sourceLocation = place.getLatLng();
                    Log.i("", "Place: " + place.getName());
                    txt_address.setText(place.getAddress().toString().replace("\n", " "));
                    this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(place.getLatLng()).zoom(15.5f).tilt(0.0f).build()));
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

    private Context getContext() {
        return MainActivity.this;
    }

    private void setupUiElements() {
        Tv_search = (TextView) findViewById(R.id.tv_search);
        Tv_service = (TextView) findViewById(R.id.tv_services);
        Tv_notification = (TextView) findViewById(R.id.tv_notification);
        Tv_account = (TextView) findViewById(R.id.tv_account);
        navBtn = (ImageButton) findViewById(R.id.nav_drawer_btn);
        btn_search = (ImageButton) findViewById(R.id.btn_search);

        setClick(R.id.rl_tab_1);
        setClick(R.id.rl_tab_2);
        setClick(R.id.rl_tab_3);
        setClick(R.id.rl_tab_4);
        setClick(R.id.nav_drawer_btn);
        setClick(R.id.btn_search);
        Tv_search.setSelected(true);
        Tv_search.setTextColor(Color.parseColor("#ED365B"));

        Show_all = (FloatingActionButton) findViewById(R.id.all_category);

        Domestic = (FloatingActionButton) findViewById(R.id.domestic);
        Construction = (FloatingActionButton) findViewById(R.id.construction);
        Events = (FloatingActionButton) findViewById(R.id.events);

        setClick(R.id.all_category);
        setClick(R.id.domestic);
        setClick(R.id.construction);
        setClick(R.id.events);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        MyApp.showMassage(this, "GoogleApi Connection failed...");
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnCameraIdleListener(this);

        View mapView = mapFragment.getView();
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 120);
        }
    }


    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15.5f).tilt(0).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            getCompleteAddressString(location.getLatitude(), location.getLongitude());

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }


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
                txt_location.setText(addresses.get(0).getSubLocality());
                if(txt_location.getText().toString().isEmpty()){
                    txt_location.setText(addresses.get(0).getLocality());
                    if(txt_location.getText().toString().isEmpty()){
                        txt_location.setText(strAdd.replace("\n", " "));
                    }
                }
                txt_address.setText(strReturnedAddress.toString().replace("\n", " "));
                Log.w("address", "" + strReturnedAddress.toString());
            } else {
                Log.w("address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void handleNewLocation(Location location) {
        sourceLocation = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            if (location != null && !isFirstSet) {
                sourceLocation = new LatLng(location.getLatitude(), location.getLongitude());
                changeMap(location);
                isFirstSet = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleManualPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1010);
    }


    private void searchRadius() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search_radius);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99000000")));

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_search = (Button) dialog.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }


    private void openImage() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attach_file);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.WRAP_CONTENT;
        lp.height = lp.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        if (position == 0) {

        } else if (position == 1) {
            MyApp.showMassage(getContext(), "will go to my service requests");
        } else if (position == 2) {
            startActivity(new Intent(getContext(), ChatActivity.class));
        } else if (position == 3) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (position == 4) {
            startActivity(new Intent(MainActivity.this, WalletActivity.class));
        } else if (position == 5) {
            MyApp.showMassage(getContext(), "will go to promos and offers");
        } else if (position == 6) {
            MyApp.showMassage(getContext(), "will switch user mode");
        } else if (position == 8) {
            MyApp.showMassage(getContext(), "will go to customer support");
        } else if (position == 7) {
            MyApp.setStatus(AppConstant.IS_LOGIN, false);
            startActivity(new Intent(getContext(), SignUpSelection.class));
            finishAffinity();
        }
    }

    private LatLng mCenterLatLong;
    private TextView txt_address;

    @Override
    public void onCameraIdle() {
        Log.d("Camera position change", this.mMap.getCameraPosition() + "");
        this.mCenterLatLong = this.mMap.getCameraPosition().target;
        this.sourceLocation = this.mCenterLatLong;
        try {
            Location mLocation = new Location("");
            mLocation.setLatitude(this.mCenterLatLong.latitude);
            mLocation.setLongitude(this.mCenterLatLong.longitude);
            getCompleteAddressString(this.mCenterLatLong.latitude, this.mCenterLatLong.longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
