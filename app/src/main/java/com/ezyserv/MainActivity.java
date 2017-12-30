package com.ezyserv;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.BookingSpinnerAdapter;
import com.ezyserv.adapter.BottomServiceAdapter;
import com.ezyserv.adapter.SpinnerAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.fragment.FragmentDrawer;
import com.ezyserv.model.NearbyServices;
import com.ezyserv.model.Services;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.ezyserv.utills.LocationProvider;
import com.ezyserv.utills.quickblox_common.QBUserHolder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.ogaclejapan.arclayout.ArcLayout;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ezyserv.application.MyApp.initializeFramworkWithApp;


public class MainActivity extends CustomActivity implements FragmentDrawer.FragmentDrawerListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationProvider.LocationCallback, LocationProvider.PermissionCallback, GoogleMap.OnCameraIdleListener,
        ResultCallback<LocationSettingsResult>, CustomActivity.ResponseCallback {

    private boolean isFirstSet = false;
    private GoogleApiClient googleApiClient;
    private LatLng sourceLocation = null;
    private LocationProvider locationProvider;
    private FragmentDrawer drawerFragment;
    //private TextView txt_location;
    private NestedScrollView bottom_sheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView bottom_sheet_recycler;
    private BottomServiceAdapter adapter;
    private List<Services> services = new ArrayList<>();
    private TextView tv_serv_catg;
    private GoogleMap mMap;
    private DrawerLayout drawer;
    private SupportMapFragment mapFragment;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private int markerPath = R.drawable.ic_handyman_marker;
    private ArcLayout arcLayout;
    private ImageButton fab;
    FrameLayout menu_arc_frame;
    private FloatingActionButton fab_events, fab_construction, fab_domestic, fab_all;
    String[] SpinnerText = {"Wallet", "Cash"};
    String[] spinnerBook = {"Book Now", "Schedule"};
    int SpinnerIcons[] = {R.drawable.wallet_white, R.drawable.cash_white};
    private Spinner wallet_cash_spiner;
    private Spinner spinner_booking;
    private TextView txt_book;
    private Switch switch_mode;
    private TextView txt_offline;
    private TextView txt_online;
    private RelativeLayout rl_provider;
    private CardView cardView;
    private CardView card_book;
    private LatLng mCenterLatLong;
    private TextView txt_address;
    private MenuItem menuRadius;

    //Hector Call
    public MediaPlayer player;
    private BroadcastReceiver mReceiveMessageFromNotification;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResponseListener(this);

        // Hector Call
        initializeFramworkWithApp(this);
        //createSessionForChat(MyApp.getApplication().readUser().getName().replaceAll(" ", ""), "12345678");
        //Call End

        player = MediaPlayer.create(this, R.raw.loud_alarm_clock);

        bottom_sheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet_recycler = findViewById(R.id.bottom_sheet_recycler);
        tv_serv_catg = findViewById(R.id.tv_serv_catg);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main4);

        //Hector Call
        setupUiElements();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setAutoMeasureEnabled(true);
        bottom_sheet_recycler.setLayoutManager(layoutManager);
        services = MyApp.getApplication().readService();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight(0);

        wallet_cash_spiner = findViewById(R.id.wallet_cash_spiner);
        spinner_booking = findViewById(R.id.spinner_booking);
        wallet_cash_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

        BookingSpinnerAdapter bookingAdapter = new BookingSpinnerAdapter(getApplicationContext(), null, spinnerBook);
        spinner_booking.setAdapter(bookingAdapter);


        locationProvider = new LocationProvider(this, this, this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        drawer = findViewById(R.id.drawer_layout);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), null);

        drawerFragment.setDrawerListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        txt_location = findViewById(R.id.txt_location);
        txt_address = findViewById(R.id.txt_address);
        menu_arc_frame = findViewById(R.id.menu_arc_frame);
        arcLayout = findViewById(R.id.arc_layout);

        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }
        fab = findViewById(R.id.fab);
        //fab_menu = findViewById(R.id.fab_menu);
        fab_events = findViewById(R.id.fab_events);
        fab_construction = findViewById(R.id.fab_construction);
        fab_domestic = findViewById(R.id.fab_domestic);
        fab_all = findViewById(R.id.fab_all);
        switch_mode = findViewById(R.id.switch_mode);
        txt_offline = findViewById(R.id.txt_offline);
        txt_online = findViewById(R.id.txt_online);
        rl_provider = findViewById(R.id.rl_provider);
        cardView = findViewById(R.id.cardView);
        card_book = findViewById(R.id.card_book);

        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_offline.setTextColor(Color.parseColor("#8A8787"));
                    txt_online.setTextColor(Color.BLACK);
                } else {
                    txt_offline.setTextColor(Color.BLACK);
                    txt_online.setTextColor(Color.parseColor("#8A8787"));
                }
                updateProviderAvailabilityStatus(isChecked);
            }
        });

        setTouchNClick(R.id.fab);
        setTouchNClick(R.id.txt_address);
        setTouchNClick(R.id.fab_all);
        setTouchNClick(R.id.fab_domestic);
        setTouchNClick(R.id.fab_construction);
        setTouchNClick(R.id.fab_events);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sourceLocation == null) {
                    locationProvider = new LocationProvider(MainActivity.this, MainActivity.this, MainActivity.this);
                    locationProvider.connect();
                }
            }
        }, (1000 * 10));


        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        bmb.clearBuilders();
        bmb.addBuilder(new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_handyman_new)
                .normalTextRes(R.string.text_ham_button_handyman).normalColor(Color.parseColor("#dd000000"))
                .highlightedColor(Color.WHITE).shadowColor(Color.GRAY));

        bmb.addBuilder(new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_events_new)
                .normalColor(Color.parseColor("#dd000000"))
                .normalTextRes(R.string.text_ham_button_events)
                .highlightedColor(Color.WHITE).shadowColor(Color.GRAY));

        bmb.addBuilder(new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_domestic_new)
                .normalColor(Color.parseColor("#dd000000"))
                .normalTextRes(R.string.text_ham_button_domestic)
                .highlightedColor(Color.WHITE).shadowColor(Color.GRAY));

        bmb.addBuilder(new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_mycare_new)
                .normalColor(Color.parseColor("#dd000000"))
                .normalTextRes(R.string.text_ham_button_my_care)
                .highlightedColor(Color.WHITE).shadowColor(Color.GRAY));

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        hideMenu();
                        List<Services.Data> allData = new ArrayList<>();
                        for (int i = 0; i < services.size(); i++) {
                            allData.addAll(services.get(i).getServices());
                        }
                        tv_serv_catg.setText("Handyman Services (" + allData.size() + ")");
                        adapter = new BottomServiceAdapter(allData, MainActivity.this, 1);
                        bottom_sheet_recycler.setAdapter(adapter);
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case 1:
                        hideMenu();
                        //  fab_menu.collapse();
                        tv_serv_catg.setText("Events Services (" + services.get(2).getServices().size() + ")");
                        adapter = new BottomServiceAdapter(services.get(2).getServices(), MainActivity.this, 2);
                        bottom_sheet_recycler.setAdapter(adapter);
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case 2:
                        hideMenu();
                        //  fab_menu.collapse();
                        tv_serv_catg.setText("Domestic Services (" + services.get(0).getServices().size() + ")");
                        adapter = new BottomServiceAdapter(services.get(0).getServices(), MainActivity.this, 0);
                        bottom_sheet_recycler.setAdapter(adapter);
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
                    case 3:
                        hideMenu();
                        tv_serv_catg.setText("My Care Services (" + services.get(1).getServices().size() + ")");
                        adapter = new BottomServiceAdapter(services.get(1).getServices(), MainActivity.this, 1);
                        bottom_sheet_recycler.setAdapter(adapter);
                        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        break;
//                        fab.setImageResource(R.drawable.events);
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });


        //Hector Call
        getMessageFromNotification();

    }


    //Hector Call
    private void getMessageFromNotification() {
        mReceiveMessageFromNotification = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //If the broadcast has received with success


                //that means device is registered successfully
                Log.e("TAG", "that means device is registered successfully");

                if (intent.getAction().equals(MyFirebaseMessagingService.MESAGE_ERROR)) {
                    Toast.makeText(context, "MESSAGE_ERROR", Toast.LENGTH_SHORT).show();
                } else if (intent.getAction().equals(MyFirebaseMessagingService.MESSAGE_NOTIFICATION)) {

                    String type = intent.getStringExtra("type");
                    Log.e(TAG, "onReceive: " + intent.getStringExtra("type"));
                    Log.e(TAG, "onReceive: " + intent.getStringExtra("userid"));
                    Log.e(TAG, "onReceive: " + intent.getStringExtra("providerid"));

                    if (type.equals("accepted")) {
                        Intent intNotif = new Intent(MainActivity.this, ChatActivity.class);
                        intNotif.putExtra("user_id", intent.getStringExtra("userid"));
                        intNotif.putExtra("serviceman_id", intent.getStringExtra("providerid"));
                        intNotif.putExtra("comeFrom", "Notif");
                        startActivity(intNotif);
                    } else if (type.equals("book_no_one")) {
                        MyApp.popMessage("Message", "No one accepted", getContext());
                        player.stop();
                    } else {
                        mNotificationManager.cancel(1);
                        if (intent.getExtras() != null) {
                            mNotificationManager.cancel(1);
                            dialogTimer();
                        }
                    }
                }
            }
        };
    }

    //End Call

    private void updateProviderAvailabilityStatus(boolean isChecked) {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        p.put("availability", isChecked ? 1 : 0);
        postCall(getContext(), AppConstant.BASE_URL + "changeAvailability", p, "", 8);
    }

    private BoomMenuButton bmb;

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

            Button btn_locate = dialog.findViewById(R.id.btn_locate);
            TextView txt_enter_manually = dialog.findViewById(R.id.txt_enter_manually);
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
        if (!MyApp.getStatus("SETTINGS_SHOWN")) {
            openTutorialView();
        }

        //Hector Call
        LocalBroadcastManager.getInstance(this).registerReceiver(pushBroadcastReceiver, new IntentFilter("new-push-event"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiveMessageFromNotification,
                new IntentFilter(MyFirebaseMessagingService.MESSAGE_NOTIFICATION));

    }


    @Override
    public void onPause() {
        super.onPause();

        //Hector Call
        Log.e("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiveMessageFromNotification);
    }

    private void openTutorialView() {

        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_radius,
                                "Radius Button",
                                "You can change radius, if you cannot find services nearby you.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(14)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true).id(3)
                                // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(50)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(android.R.color.black)
                                .transparentTarget(true)
                                .textColor(android.R.color.black)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
//                        ((TextView) findViewById(R.id.educated)).setText("Congratulations! You're educated now!");
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                        MyApp.setStatus("SETTINGS_SHOWN", true);
//                        startActivity(new Intent(getContext(), MainActivity.class));
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
//                        https://github.com/KeepSafe/TapTargetView
//                        https://github.com/KeepSafe/TapTargetView/blob/master/app/src/main/java/com/getkeepsafe/taptargetviewsample/MainActivity.java
                    }
                });
        sequence.start();
    }


    private void openSearchServiceView() {

        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(bmb,
                                "Search service",
                                "You can select your required service by click this button.")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(14)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true).id(3)
                                // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(50)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(android.R.color.black)
                                .transparentTarget(true)
                                .textColor(android.R.color.white)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
//                        ((TextView) findViewById(R.id.educated)).setText("Congratulations! You're educated now!");
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                        MyApp.setStatus("SETTINGS_SHOWN", true);
//                        startActivity(new Intent(getContext(), MainActivity.class));
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
//                        https://github.com/KeepSafe/TapTargetView
//                        https://github.com/KeepSafe/TapTargetView/blob/master/app/src/main/java/com/getkeepsafe/taptargetviewsample/MainActivity.java
                    }
                });
        sequence.start();
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
        menuRadius = menu.findItem(R.id.action_radius);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_radius) {
            searchRadius();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.txt_book) {

            String sId = "";
            for (String s : servicesIDs) {
                sId += s + ",";
            }
            sId = sId.substring(0, sId.length() - 1);
            if (spinner_booking.getSelectedItemPosition() == 0) {
                RequestParams p = new RequestParams();
                p.put("serviceman_ids", sId);
                p.put("user_id", MyApp.getApplication().readUser().getId());
                p.put("service_id", "");
                p.put("service_lat", sourceLocation.latitude + "");
                p.put("service_long", sourceLocation.longitude + "");
                p.put("service_time", "");
                p.put("service_address", txt_address.getText().toString());
                p.put("service_type ", ""); //general, emergency ,scheduled
                p.put("service_description ", "service");
                searchWithinRadius("Searching you a serviceman");
                postCall(getContext(), AppConstant.BASE_URL + "createService", p, "", 10);
            } else {
                startActivity(new Intent(getContext(), ScheduleServiceActivity.class));
            }

        } /*else if (v == navBtn) {
            drawer.openDrawer(GravityCompat.START);
        }*//* else if (v == txt_location) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
            MainActivity.this.startActivityForResult(intent, 122);
        }*/ else if (v == txt_address) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(AppConstant.EXTRA_1, "Enter your location");
            MainActivity.this.startActivityForResult(intent, 122);
        } /*else if (v == btn_search) {
            searchRadius();
        }*/ else if (v == fab_all) {
            fab.setImageResource(R.drawable.all_cats);
            hideMenu();
            //  fab_menu.collapse();
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (v == fab_domestic) {
            fab.setImageResource(R.drawable.domestic);
            hideMenu();
            // fab_menu.collapse();
            tv_serv_catg.setText("Domestic Services ((" + services.get(0).getServices().size() + ")");
//            adapter = new BottomServiceAdapter(services, this, 0);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (v == fab_construction) {
            fab.setImageResource(R.drawable.construction);
            hideMenu();
            //  fab_menu.collapse();
            tv_serv_catg.setText("Construction Services (" + services.get(1).getServices().size() + ")");
//            adapter = new BottomServiceAdapter(services, this, 1);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (v == fab_events) {
            fab.setImageResource(R.drawable.events);
            hideMenu();
            //  fab_menu.collapse();
            tv_serv_catg.setText("Events Services (" + services.get(2).getServices().size() + ")");
//            adapter = new BottomServiceAdapter(services, this, 2);
            bottom_sheet_recycler.setAdapter(adapter);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (v == fab) {


//            if (v.isSelected()) {
//                hideMenu();
//            } else {
//                showMenu();
//            }
//            v.setSelected(!v.isSelected());
        }
    }

    private void showMenu() {
        menu_arc_frame.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menu_arc_frame.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }


    private Animator createHideItemAnimator(final View item) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
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
        txt_book = findViewById(R.id.txt_book);
        setTouchNClick(R.id.txt_book);
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
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 300, 30, 0);
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

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                } else {
                    try {
                        strAdd = returnedAddress.getAddressLine(0);
                    } catch (Exception ignored) {
                    }
                }


                txt_address.setText(strAdd.replace("\n", " "));
                if (strAdd.isEmpty()) {
                    String alterAdd = "";
                    alterAdd = addresses.get(0).getSubLocality();
                    if (alterAdd.isEmpty() && strAdd.isEmpty()) {
                        alterAdd = addresses.get(0).getLocality();
                        if (alterAdd.isEmpty()) {
                            alterAdd = strAdd.replace("\n", " ");
                        }
                    }
                    txt_address.setText(alterAdd);
                }
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

    private void getNearByServices(double lat, double lng, String serviceId, String radius) {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        p.put("service_id", serviceId);
        p.put("current_lat", lat);
        p.put("current_lng", lng);
        p.put("radius", radius);
        postCall(getContext(), AppConstant.BASE_URL + "Autoassign", p, "Collecting data...", 1);
    }

    @Override
    public void handleNewLocation(Location location) {
        sourceLocation = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            if (location != null && !isFirstSet) {
                sourceLocation = new LatLng(location.getLatitude(), location.getLongitude());
                changeMap(location);
                isFirstSet = true;
                updateProfileLocation(location);
//                getNearByServices(sourceLocation.latitude, sourceLocation.longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfileLocation(Location location) {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        p.put("currentlat", location.getLatitude() + "");
        p.put("currentlong", location.getLongitude() + "");
        p.put("device_token", MyApp.getSharedPrefString(AppConstant.DEVICE_TOKEN));
        p.put("device_type", "Android");
        postCall(getContext(), AppConstant.BASE_URL + "updateProfile", p, "", 7);
        if (!MyApp.getStatus(AppConstant.IS_SERVICES_UPDATE)) {
            updateServices();
        }

    }

    private void updateServices() {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        p.put("primaryservices", MyApp.getSharedPrefString(AppConstant.PRIMARY_SERVICE_ID));
        p.put("services", MyApp.getSharedPrefString(AppConstant.SECONDARY_SERVICES).replace(" ", ""));

        postCall(getContext(), AppConstant.BASE_URL + "updateServices", p, "", 9);
    }

    @Override
    public void handleManualPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1010);
    }


    private void searchRadius() {
        if (selectedService.isEmpty()) {
            openSearchServiceView();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search_radius);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99000000")));

        TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
        final TextView txt_radius = dialog.findViewById(R.id.txt_radius);

        final DiscreteSeekBar prog_bar = dialog.findViewById(R.id.prog_bar);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        prog_bar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                txt_radius.setText(value + " miles");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        Button btn_search = dialog.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNearByServices(sourceLocation.latitude, sourceLocation.longitude, selectedService, prog_bar.getProgress() + "");
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

    private String selectedService = "";

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
            startActivity(new Intent(getContext(), ChattingListActivity.class));
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
            try {
                if (MyApp.getApplication().readUser().getAvailability().equals("0")) {
                    isActive = false;
                } else {
                    isActive = true;
                }
                switch_mode.setChecked(isActive);
            } catch (Exception e) {
            }
            if (isUser) {
                menuRadius.setVisible(false);
                card_book.setVisibility(View.GONE);
                bmb.setVisibility(View.GONE);
                drawerFragment.nav_item_switch_profile.setText("Switch to User");
                cardView.setVisibility(View.GONE);
                rl_provider.setVisibility(View.VISIBLE);
            } else {
                menuRadius.setVisible(true);
                card_book.setVisibility(View.VISIBLE);
                bmb.setVisibility(View.VISIBLE);
                drawerFragment.nav_item_switch_profile.setText("Switch to Service Provider");
                cardView.setVisibility(View.VISIBLE);
                rl_provider.setVisibility(View.GONE);
            }
            isUser = !isUser;
        }
    }

    private boolean isUser = true;
    private boolean isActive = true;

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

    private List<String> servicesIDs = new ArrayList<>();

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
//            removeSearch();
            if (o.optString("status").equals("true")) {
                Type listType = new TypeToken<List<NearbyServices.Data>>() {
                }.getType();
                try {
                    //  allServices = gson.fromJson(o.getJSONArray("data").toString(), listType);
                    List<NearbyServices.Data> nearBy = new Gson().fromJson(o.getJSONArray("data").toString(), listType);
                    servicesIDs.clear();
                    if (nearBy.size() > 0) {
                        mMap.clear();
                        txt_book.setEnabled(true);
                        txt_book.setTextColor(Color.WHITE);
                    } else {
                        txt_book.setEnabled(false);
                        txt_book.setTextColor(Color.GRAY);
                    }
                    for (int i = 0; i < nearBy.size(); i++) {
                        if (nearBy.get(i).getUser_id().equals(MyApp.getApplication().readUser().getId())) {
                            return;
                        }
                        servicesIDs.add(nearBy.get(i).getUser_id());
//                        if (i == 0) {
//                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble(nearBy.get(i).getCurrentlat()), Double.parseDouble(nearBy.get(i).getCurrentlong()))).zoom(15.5f).tilt(0.0f).build()));
//
//                        }
                        if (nearBy.get(i).getService_categories_id().equals("7")) {
                            markerPath = R.drawable.ic_domestic_marker;

                        } else if (nearBy.get(i).getService_categories_id().equals("8")) {
                            markerPath = R.drawable.ic_mycare_marker;

                        } else if (nearBy.get(i).getService_categories_id().equals("9")) {
                            markerPath = R.drawable.ic_event_marker;

                        } else {
                            markerPath = R.drawable.ic_handyman_marker;
                        }


                        //Hector Call
                        /*Marker For the Map Services*/
                        Marker m1 = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(this.sourceLocation.latitude, this.sourceLocation.longitude))
                                .title(nearBy.get(i).getService_name())
                                .icon(BitmapDescriptorFactory.fromResource(markerPath)));

                        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(new LatLng(this.sourceLocation.latitude, this.sourceLocation.longitude));
                        Marker m1 = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(nearBy.get(i).getCurrentlat()),
                                Double.parseDouble(nearBy.get(i).getCurrentlong()) - 0.0901))
                                .icon(BitmapDescriptorFactory.fromResource(markerPath)));
                        m1.setSnippet(nearBy.get(i).getName());
                        m1.setTitle(nearBy.get(i).getService_name());
                        builder.include(m1.getPosition());*/
                    }

                    if (servicesIDs.size() == 0) {
                        txt_book.setEnabled(false);
                        txt_book.setTextColor(Color.GRAY);
                        MyApp.popMessage("Alert!", o.optString("No service provider is nearby to you. Try to change radius and search again.\nThank you"), MainActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyApp.popMessage("Alert!", "Parsing error.", getContext());
                    return;
                } catch (JsonSyntaxException ee) {
                }
            } else {
                MyApp.popMessage("Alert!", o.optString("Message"), MainActivity.this);
                return;
            }

        } else if (callNumber == 8) {
            if (o.optString("status").equals("true")) {
                try {
                    User existingUser = MyApp.getApplication().readUser();
                    User u = new Gson().fromJson(o.getJSONObject("data").toString(), User.class);
                    existingUser.setActive(u.getActive());
                    existingUser.setAvailability(u.getAvailability());
                    existingUser.setCurrentActive(u.getCurrentActive());
                    MyApp.getApplication().writeUser(existingUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (callNumber == 5) {

        } else if (callNumber == 9) {
            if (o.optString("status").equals("true")) {
                MyApp.setStatus(AppConstant.IS_SERVICES_UPDATE, true);
            }
        } else if (callNumber == 12) {
            if (o.optString("status").equals("true")) {
                getCreateServiceDetails();
            }
        } else if (callNumber == 10) {
            removeSearch();
            //startActivity(new Intent(MainActivity.this, PaymentSelectionActivity.class)
            //.putExtra("isShedule", wallet_cash_spiner.getSelectedItemPosition()));
        } else if (callNumber == 13) {
            if (o.optString("status").equals("true")) {
                try {
                    JSONObject dataJsonObject = o.getJSONObject("data");
                    Log.e(TAG, "getCreateServiceDetails: Reponse" + dataJsonObject.toString());
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("user_id", dataJsonObject.getString("user_id"));
                    intent.putExtra("serviceman_id", dataJsonObject.getString("serviceman_id"));
                    intent.putExtra("comeFrom", "Notif");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //Hector Call
    //Show Timer Dialog If App Is Open
    void getCreateServiceDetails() {
        Log.e(TAG, "getCreateServiceDetails: Called" + MyApp.getSharedPrefString(AppConstant.REQUESTED_SERVICE_ID));
        RequestParams p = new RequestParams();
        p.put("create_service_id", MyApp.getSharedPrefString(AppConstant.REQUESTED_SERVICE_ID));
        postCall(getContext(), AppConstant.BASE_URL + "getCreateServiceDetails", p, "", 13);
    }

    //End Call

    private int progress;

    //Hector Call
    /*Dialog to Display the user request if app is open and will count down for 20 seconds*/
    private void dialogTimer() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_book_request);
        dialog.setCancelable(false);
        final TextView txt_timer = dialog.findViewById(R.id.txt_timer);
        txt_timer.setVisibility(View.GONE);
        final CircleCountDownView countDownView = dialog.findViewById(R.id.countDownView);
        Button btn_denied_req = dialog.findViewById(R.id.btn_denied_req);
        Button btn_accept_req = dialog.findViewById(R.id.btn_accept_req);
        progress = 1;
        final CountDownTimer countDownTimer2 = new CountDownTimer((long) 30000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownView.setProgress(progress, 20);
                progress = progress + 1;
            }

            public void onFinish() {
                countDownView.setProgress(progress, 20);
                dialog.dismiss();
//                mp.stop();
                dialog.dismiss();
//                isTimerDialogShown = false;
            }
        };
        countDownTimer2.start();
        int time = 20 * 1000;
        final CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mNotificationManager.cancel(1);
                dialog.dismiss();
            }

        }.start();
        btn_denied_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mNotificationManager.cancel(1);
                countDownTimer.cancel();
                countDownTimer2.cancel();
                player.stop();
            }
        });
        btn_accept_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                mNotificationManager.cancel(1);
                countDownTimer.onFinish();
                countDownTimer.cancel();
                countDownTimer2.cancel();
                RequestParams p = new RequestParams();
                p.put("createService_id", MyApp.getSharedPrefString(AppConstant.REQUESTED_SERVICE_ID));
                p.put("service_status", "accepted");
                postCall(getContext(), AppConstant.BASE_URL + "respondService", p, "", 12);

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //End Call

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {

    }

    public void searchCategoryById(String service_id) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        selectedService = service_id;
        if (sourceLocation != null) {
            getNearByServices(sourceLocation.latitude, sourceLocation.longitude, service_id, "5");
        } else
            MyApp.popMessage("Alert!", "Location not loaded yet", getContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProvider = new LocationProvider(this, this, this);
        locationProvider.connect();
    }


    //Hector Call

    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intnt) {

            Log.e(TAG, "onReceive: Notification" );

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.ic_check_black_24dp)
                            .setContentTitle("New Message")
                            .setContentText(intent.getStringExtra("message"))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(15 /* ID of notification */, notificationBuilder.build());

        }
    };
    //Call End
}
