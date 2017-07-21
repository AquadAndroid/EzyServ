package com.ezyserv;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ezyserv.custome.CustomActivity;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/*import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;*/

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends CustomActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,LocationProvider.LocationCallback, LocationProvider.PermissionCallback,
        ResultCallback<LocationSettingsResult>{

    private boolean isFirstSet = false;
    private GoogleApiClient googleApiClient;
    private LatLng sourceLocation = null;
    private LocationProvider locationProvider;



    ImageButton search_tab, service_tab, notification_tab, account_tab;
    private GoogleMap mMap;
    DrawerLayout drawer;
    private SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "MainActivity";
    TextView mLocationText;
    TextView Tv_search, Tv_service, Tv_notification, Tv_account;
    ImageButton navBtn;

    FloatingActionButton Show_all, Domestic, Construction, Events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUiElements();
        locationProvider = new LocationProvider(this,this,this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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


    @Override
    protected void onStart() {
        super.onStart();
        locationProvider.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!MyApp.isLocationEnabled(this)) {
            enableGPS();
        }
        if (sourceLocation != null && isFirstSet) {
//            currentLocation.distanceTo()
           // getNearbyDrivers(sourceLocation.latitude + "", sourceLocation.longitude + "");
        }


    }

   /* public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
*/
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
          startActivity(new Intent(MainActivity.this, PaymentSelectionActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v == search_tab) {
            search_tab.setSelected(true);
            service_tab.setSelected(false);
            notification_tab.setSelected(false);
            account_tab.setSelected(false);
            search_tab.setImageResource(R.drawable.search_active);
            notification_tab.setImageResource(R.drawable.notifications_inactive);
            service_tab.setImageResource(R.drawable.services_inactive);
            account_tab.setImageResource(R.drawable.account_inactive);


            Tv_search.setTextColor(Color.parseColor("#ED365B"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));
        } else if (v == service_tab) {
            search_tab.setSelected(false);
            service_tab.setSelected(true);
            notification_tab.setSelected(false);
            account_tab.setSelected(false);

            search_tab.setImageResource(R.drawable.search_inactive);
            notification_tab.setImageResource(R.drawable.notifications_inactive);
            service_tab.setImageResource(R.drawable.services_active);
            account_tab.setImageResource(R.drawable.account_inactive);

            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#ED365B"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));
startActivity(new Intent(MainActivity.this, PaymentSelectionActivity.class));
        } else if (v == notification_tab) {
            search_tab.setSelected(false);
            service_tab.setSelected(false);
            notification_tab.setSelected(true);
            account_tab.setSelected(false);

            search_tab.setImageResource(R.drawable.search_inactive);
            notification_tab.setImageResource(R.drawable.notifications_active);
            service_tab.setImageResource(R.drawable.services_inactive);
            account_tab.setImageResource(R.drawable.account_inactive);


            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#ED365B"));
            Tv_account.setTextColor(Color.parseColor("#3949AB"));

            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (v == account_tab) {
            search_tab.setSelected(false);
            service_tab.setSelected(false);
            notification_tab.setSelected(false);
            account_tab.setSelected(true);
            search_tab.setImageResource(R.drawable.search_inactive);
            notification_tab.setImageResource(R.drawable.notifications_inactive);
            service_tab.setImageResource(R.drawable.services_inactive);
            account_tab.setImageResource(R.drawable.account_active);


            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#ED365B"));
            searchRadius();
        }else if(v == navBtn){
            drawer.openDrawer(GravityCompat.START);

        }


        }





    private void setupUiElements() {
        search_tab=(ImageButton)findViewById(R.id.btn_tab_1);
        Tv_search=(TextView)findViewById(R.id.tv_search);
        service_tab=(ImageButton)findViewById(R.id.btn_tab_2);
        Tv_service=(TextView)findViewById(R.id.tv_services);
        notification_tab=(ImageButton)findViewById(R.id.btn_tab_3);
        Tv_notification=(TextView)findViewById(R.id.tv_notification);
        account_tab=(ImageButton)findViewById(R.id.btn_tab_4);
        Tv_account=(TextView)findViewById(R.id.tv_account);
        navBtn=(ImageButton)findViewById(R.id.nav_drawer_btn) ;

        setClick(R.id.btn_tab_1);
        setClick(R.id.btn_tab_2);
        setClick(R.id.btn_tab_3);
        setClick(R.id.btn_tab_4);
        setClick(R.id.nav_drawer_btn);
        search_tab.setSelected(true);
        Tv_search.setTextColor(Color.parseColor("#ED365B"));

        Show_all=(FloatingActionButton)findViewById(R.id.all_category);
        Domestic=(FloatingActionButton)findViewById(R.id.domestic);
        Construction=(FloatingActionButton)findViewById(R.id.construction);
        Events=(FloatingActionButton)findViewById(R.id.events);

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
            layoutParams.setMargins(0, 0, 30, 90);
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

//            mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            //startIntentService(location);


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
                mLocationText.setText(strReturnedAddress.toString());
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





    private void searchRadius(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search_radius);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#22000000")));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }



}



   /* protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }*/

