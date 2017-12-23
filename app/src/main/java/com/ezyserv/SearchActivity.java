package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.PlaceAutocompleteAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.utills.AppConstant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by SONI on 5/15/2017.
 */

public class SearchActivity extends CustomActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnClickListener {
    Context mContext;
    GoogleApiClient mGoogleApiClient;

    LinearLayout mParent;
    private RecyclerView mRecyclerView;
    private TextView tv_saved_address;
    LinearLayoutManager llm;

    PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    EditText mSearchEdittext;
    ImageView mClear;
    private ImageButton img_btn_search_back;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = SearchActivity.this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        tv_saved_address = findViewById(R.id.tv_saved_address);
        img_btn_search_back = findViewById(R.id.img_btn_search_back);
        if (MyApp.getSharedPrefString(AppConstant.LOCATION).equals(null) || MyApp.getSharedPrefString(AppConstant.LOCATION).isEmpty()) {
            tv_saved_address.setVisibility(View.GONE);

        } else {
            tv_saved_address.setVisibility(View.VISIBLE);
            tv_saved_address.setBackgroundResource(R.drawable.search_back);
            tv_saved_address.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rating_yellow_big, 0);
            tv_saved_address.setText(MyApp.getSharedPrefString(AppConstant.LOCATION));
        }

        setTouchNClick(R.id.img_btn_search_back);

        initViews();
    }


    /*
   Initialize Views
    */
    private void initViews() {
        mRecyclerView = findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = findViewById(R.id.search_et);
        mSearchEdittext.setHint(getIntent().getStringExtra(AppConstant.EXTRA_1));
        mClear = findViewById(R.id.clear);
        mClear.setOnClickListener(this);

        // Hector Call
        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch, mGoogleApiClient, BOUNDS_INDIA, null);
        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
//                    mClear.setVisibility(View.GONE);
//                    if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
//                        mRecyclerView.setAdapter(mSavedAdapter);
//                    }
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == mClear) {
            mSearchEdittext.setText("");
            if (mAdapter != null) {
                mAdapter.clearList();
            }
        } else if (v == img_btn_search_back) {
            finish();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            SingleInstance.getInstance().setSelectedPlace(places.get(0));
                            //Do the things here on Click.....
                            Intent data = new Intent();
                            data.putExtra("lat", String.valueOf(places.get(0).getLatLng().latitude));
                            data.putExtra("lng", String.valueOf(places.get(0).getLatLng().longitude));
                            setResult(SearchActivity.RESULT_OK, data);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

//    @Override
//    public void onSavedPlaceClick(List<SavedAddress> mResponse, int position) {
//        if(mResponse!=null){
//            try {
//                Intent data = new Intent();
//                data.putExtra("lat",String.valueOf(mResponse.get(position).getLatitude()));
//                data.putExtra("lng", String.valueOf(mResponse.get(position).getLongitude()));
//                setResult(SearchActivity.RESULT_OK, data);
//                finish();
//            }
//            catch (Exception e){
//
//            }
//
//        }
//    }
}