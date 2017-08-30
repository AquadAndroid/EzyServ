package com.ezyserv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceDetailActivityTwo extends CustomActivity implements CustomActivity.ResponseCallback {
    private Toolbar Dtoolbar;
    private TextView tvDomestic, tvConstruction, tvEvents, tvAddLocation;
    private TextView countDomestic, countConstruction, countEvents;
    private RecyclerView listLocations;
    private Button Continue;
    private ArrayList listdata;
    private ServiceLocationAdapter adapter;
    private ScrollView scrollView;
    private List<Services> allServices = null;
    String strEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_service_detail_two);
        Dtoolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_title);
        TextView mCount = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_count);
        mTitle.setText("Add your details");
        mCount.setText("2/2");
        actionBar.setTitle("");

        setupUiElement();
        countDomestic.setText(strEditText+"services added");
        getAllServices();
    }


    private void getAllServices() {
        getCall(getContext(), AppConstant.BASE_URL + "getAllServices", "Loading services...", 1);
    }

    private Context getContext() {
        return ServiceDetailActivityTwo.this;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
               strEditText = data.getStringExtra("editTextValue");
                Toast.makeText(this, ""+strEditText, Toast.LENGTH_SHORT).show();
               // countDomestic.setText(strEditText+"services added");
            }
        }
    }

    private void setupUiElement() {

        setTouchNClick(R.id.tv_domestic);
        setTouchNClick(R.id.tv_construction);
        setTouchNClick(R.id.tv_events);
        setTouchNClick(R.id.btn_continue);


        tvDomestic = (TextView) findViewById(R.id.tv_domestic);
        tvConstruction = (TextView) findViewById(R.id.tv_construction);
        tvEvents = (TextView) findViewById(R.id.tv_events);
        tvAddLocation = (TextView) findViewById(R.id.edt_add_loaction);


        countDomestic = (TextView) findViewById(R.id.tv_domestic_label);
       // countDomestic.setText(returnValue+"services added");
        countConstruction = (TextView) findViewById(R.id.tv_construction_label);
        countEvents = (TextView) findViewById(R.id.tv_events_label);
        listdata = (ArrayList) DummyLocation.getListData();
        listLocations = (RecyclerView) findViewById(R.id.location_list);

        listLocations.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ServiceLocationAdapter(listdata, this);
        listLocations.setAdapter(adapter);

        Continue = (Button) findViewById(R.id.btn_continue);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
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
                //countDomestic.setText(allServices.size());
                //allServices.size();
                return;
            }
            if (allServices.size() == 0) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key", "domestic");
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
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(2));
            startActivity(intent);
        } else if (v.getId() == R.id.btn_continue) {
            //Toast.makeText(this, "The Project is under Process will be updated soon", Toast.LENGTH_LONG).show();
            dialogLocation();
        }

    }


    public void dialogLocation() {

//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        dialog.setContentView(R.layout.location_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialog.show();
        MyApp.setStatus(AppConstant.IS_LOGIN, true);
        startActivity(new Intent(ServiceDetailActivityTwo.this, MainActivity.class));
        finishAffinity();

    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
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
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());
    }
}
