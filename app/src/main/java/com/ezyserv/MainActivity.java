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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.BottomServiceAdapter;
import com.ezyserv.adapter.CustomAdapterTwo;
import com.ezyserv.adapter.SpinnerAdapter;
import com.ezyserv.adapter.SwipeDeckAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.fragment.FragmentDrawer;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
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

    private NestedScrollView bottom_sheet;
    private BottomSheetBehavior bottomSheetBehavior;
    //  private GridView service_gridview;
    private RecyclerView bottom_sheet_recycler;
    private BottomServiceAdapter adapter;
    // private Services services;
    private List<Services> services = new ArrayList<>();
    private TextView tv_serv_catg;
    private GoogleMap mMap;
    private DrawerLayout drawer;
    private SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "MainActivity";
    private TextView Tv_search, Tv_service, Tv_notification, Tv_account;
    private ImageButton navBtn, btn_search;

    FloatingActionButton Show_all, Domestic, Construction, Events;
    String[] SpinnerText = {"Wallet", "Cash"};
    int SpinnerIcons[] = {R.drawable.wallet_white, R.drawable.cash_white};
    private Spinner wallet_cash_spiner;
    private TextView tv_book_now;
    private int pos = 0;

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
/*SplashActivity splash= new SplashActivity();
        splash.getServices();*/

        bottom_sheet = (NestedScrollView) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        tv_serv_catg=(TextView)findViewById(R.id.tv_serv_catg);
        bottom_sheet_recycler = (RecyclerView) findViewById(R.id.bottom_sheet_recycler);
        bottom_sheet_recycler.setLayoutManager(new GridLayoutManager(this, 4));
        services = MyApp.getApplication().readService();
      /*  adapter = new BottomServiceAdapter(services, this, pos);
        bottom_sheet_recycler.setAdapter(adapter);*/

        /*ServiceList = (RecyclerView) findViewById(R.id.service_recycle);
        ServiceList.setLayoutManager(new LinearLayoutManager(this));
        services = SingleInstance.getInstance().getSelectedServiceCategory();
        adapter = new AddServiceAdapter(services, this);
        ServiceList.setAdapter(adapter);*/

       /* service_gridview = (GridView) findViewById(R.id.service_gridview);
        allProducts = MyApp.getApplication().readService();
        CustomAdapterTwo customAdaptertwo = new CustomAdapterTwo(getContext(), allProducts);
        service_gridview.setAdapter(customAdaptertwo);*/
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Toast.makeText(MainActivity.this, "expanded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight(0);

        wallet_cash_spiner = (Spinner) findViewById(R.id.wallet_cash_spiner);
        wallet_cash_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                 Toast.makeText(getApplicationContext(), SpinnerText[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getBooleanExtra("showAlert", false)) {
            MyApp.popMessage("Message", "You are redirected to this activity even the verification of the " +
                    "provider is under development." +
                    " You will find this work in next update, Sorry for the inconvenience.", getContext());
        }

        SpinnerAdapter customAdapter = new SpinnerAdapter(getApplicationContext(), SpinnerIcons, SpinnerText);
        wallet_cash_spiner.setAdapter(customAdapter);
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

     /* //  SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);
        List<String> dataList = new ArrayList<>();
        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(dataList, this);
        cardStack.setAdapter(adapter);
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });*/
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

            startActivity(new Intent(MainActivity.this, AllServicesActivity.class));

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
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (v.getId() == R.id.rl_tab_4) {
            Tv_search.setSelected(false);
            Tv_service.setSelected(false);
            Tv_notification.setSelected(false);
            Tv_account.setSelected(true);

            Tv_search.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search_inactive, 0, 0);
            Tv_notification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.notifications_inactive, 0, 0);
            Tv_service.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.services_inactive, 0, 0);
            Tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0);

//// pullllllllllllll
            Tv_search.setTextColor(Color.parseColor("#3949AB"));
            Tv_service.setTextColor(Color.parseColor("#3949AB"));
            Tv_notification.setTextColor(Color.parseColor("#3949AB"));
            Tv_account.setTextColor(Color.parseColor("#ED365B"));
            //  startActivity(new Intent(MainActivity.this, AddMoneyActivity.class));

            // startActivity(new Intent(MainActivity.this, ScheduleServiceActivity.class));

        } else if (v.getId() == R.id.tv_book_now) {

            startActivity(new Intent(MainActivity.this, PaymentSelectionActivity.class));


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
            tv_serv_catg.setText("Handyman Services (8)");
            adapter = new BottomServiceAdapter(services, this, 0);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            // startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Domestic) {
            tv_serv_catg.setText("Domestic Services (8)");
            adapter = new BottomServiceAdapter(services, this, 0);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            // startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Construction) {
            tv_serv_catg.setText("Construction Services (7)");
            adapter = new BottomServiceAdapter(services, this, 1);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            // /startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (v == Events) {
            tv_serv_catg.setText("Events Services (9)");
            adapter = new BottomServiceAdapter(services, this, 2);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            //  startActivity(new Intent(getContext(), SearchingServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        tv_book_now = (TextView) findViewById(R.id.tv_book_now);

       /* wallet_cash_spiner = (Spinner) findViewById(R.id.wallet_cash_spiner);
       // wallet_cash_spiner.setOnItemSelectedListener(this);*/


        setClick(R.id.tv_book_now);

        setClick(R.id.all_category);
        setClick(R.id.domestic);
        setClick(R.id.construction);
        setClick(R.id.events);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
//            changeMap(mLastLocation);
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
            layoutParams.setMargins(0, 0, 30, 300);
        }
    }


    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
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

    private List<Marker> markers = new ArrayList<>();

    private LatLngBounds adjustBoundsForMaxZoomLevel(LatLngBounds bounds) {
        LatLng sw = bounds.southwest;
        LatLng ne = bounds.northeast;
        double deltaLat = Math.abs((sw.latitude - this.sourceLocation.latitude) - (ne.latitude - this.sourceLocation.latitude));
        double deltaLon = Math.abs((sw.longitude - this.sourceLocation.longitude) - (ne.longitude - this.sourceLocation.longitude));
        LatLng latLng;
        LatLng ne2;
        LatLngBounds latLngBounds;
        if (deltaLat < 0.005d) {
            latLng = new LatLng(sw.latitude - (0.005d - (deltaLat / 2.0d)), sw.longitude);
            ne2 = new LatLng(ne.latitude + (0.005d - (deltaLat / 2.0d)), ne.longitude);
            latLngBounds = new LatLngBounds(latLng, ne2);
            ne = ne2;
            sw = latLng;
        } else if (deltaLon < 0.005d) {
            latLng = new LatLng(sw.latitude, sw.longitude - (0.005d - (deltaLon / 2.0d)));
            ne2 = new LatLng(ne.latitude, ne.longitude + (0.005d - (deltaLon / 2.0d)));
            latLngBounds = new LatLngBounds(latLng, ne2);
            ne = ne2;
            sw = latLng;
        }
        LatLngBounds.Builder displayBuilder = new LatLngBounds.Builder();
        displayBuilder.include(new LatLng(this.sourceLocation.latitude, this.sourceLocation.longitude));
        displayBuilder.include(new LatLng(this.sourceLocation.latitude + deltaLat, this.sourceLocation.longitude + deltaLon));
        displayBuilder.include(new LatLng(this.sourceLocation.latitude - deltaLat, this.sourceLocation.longitude - deltaLon));
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(displayBuilder.build(), 100));
        this.mMap.setMaxZoomPreference(15.5f);
        return bounds;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(this.sourceLocation.latitude, this.sourceLocation.longitude));
        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE + 0.2001, LONGITUDE - 0.0901))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m1.getPosition());
        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE + 0.05315, LONGITUDE - 0.1551))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m2.getPosition());
        Marker m3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE - 0.0971, LONGITUDE + 0.4091))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m3.getPosition());
        Marker m4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE - 0.00914, LONGITUDE + 0.0241))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m4.getPosition());
        Marker m5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE - 0.0001, LONGITUDE + 0.1501))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m5.getPosition());
        Marker m6 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(LATITUDE + 0.1001, LONGITUDE - 0.0231))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceman_on_map)));
        builder.include(m6.getPosition());
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(adjustBoundsForMaxZoomLevel(builder.build()), 50);

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
                if (txt_location.getText().toString().isEmpty()) {
                    txt_location.setText(addresses.get(0).getLocality());
                    if (txt_location.getText().toString().isEmpty()) {
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
            //  MyApp.showMassage(getContext(), "will go to my service requests");
            startActivity(new Intent(getContext(), ScheduleServiceActivity.class));
        } else if (position == 1) {
            MyApp.showMassage(getContext(), "will go to my service requests");
        } else if (position == 2) {
            startActivity(new Intent(getContext(), ChatActivity.class));
        } else if (position == 3) {
            MyApp.showMassage(getContext(), "will go to History");
        } else if (position == 4) {
            startActivity(new Intent(MainActivity.this, WalletActivity.class));
        } else if (position == 5) {
            MyApp.showMassage(getContext(), "will go to promos and offers");
        } else if (position == 6) {
            MyApp.showMassage(getContext(), "will go to Favourites");
        } else if (position == 7) {

            String shareBody = "https://play.google.com/store/apps/details?" + "id=com.ezyserv";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "EzyServ (Open it in Google Play Store to Download the Application)");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            //  MyApp.showMassage(getContext(), "will invite your friends");
        } else if (position == 8) {
            MyApp.showMassage(getContext(), "will switch to service mode");
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

    public void serviceMenDetails() {
        startActivity(new Intent(MainActivity.this, ServicemanProfileActivity.class));
    }
}
