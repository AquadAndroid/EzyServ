package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ezyserv.adapter.DummyLocation;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChangeServicesActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    String TAG = ChangeServicesActivity.class.getSimpleName();
    private Toolbar Dtoolbar;
    private TextView tv_domestic_label, txt_mycare_counts, tv_events_label, tv_primary_lable;
    private Button Continue;
    private ArrayList listdata;
    private ScrollView scrollView;
    private List<Services> allServices = null;
    private boolean isCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCompany = getIntent().getBooleanExtra("isCompany", false);
        isCompany = true;
        setResponseListener(this);
        setContentView(R.layout.activity_service_update);
        Dtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = Dtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Update Services");
        actionBar.setTitle("");

        setupUiElement();
        tv_domestic_label.setText("0 services added");
        getAllServices();
        User u = MyApp.getApplication().readUser();

        try {
            SingleInstance.getInstance().setPrimaryName(u.getService().getPrimary().get(0).getName() + "@" + u.getService().getPrimary().get(0).getId());
        } catch (Exception e) {
            Log.e(TAG, "onCreate:first catch  " + e.toString());
        }


        //Assigning the Selected Serivces Ids to Single Instance
       /* try {
            String ids = "";
            for (int i = 0; i < u.getService().getSecondary().size(); i++) {
                ids += u.getService().getSecondary().get(i).getId() + ",";
                SingleInstance.getInstance().setServicesId(ids);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: third catch " + e.toString());
        }*/
    }

    private String primaryServiceId = "";

    private boolean isFirstDone = false;
    private boolean isDomesticClicked = false;
    private boolean isCareClicked = false;
    private boolean isEventsClicked = false;

    @Override
    protected void onResume() {
        super.onResume();
        User u = MyApp.getApplication().readUser();
        if (!isFirstDone) {
            isFirstDone = true;
            String domesticNames = "";
            String eventsNames = "";
            String myCareNames = "";
            try {
                for (int i = 0; i < u.getService().getSecondary().size(); i++) {
                    for (int j = 0; j < allServices.get(0).getServices().size(); j++) {
                        //domestic
                        if (allServices.get(0).getServices().get(j).getId().equals(u.getService().getSecondary().get(i).getId())) {
                            domesticNames += u.getService().getSecondary().get(i).getName() + ", ";
                        }
                    }
                    for (int j = 0; j < allServices.get(1).getServices().size(); j++) {
                        //construction
                        if (allServices.get(1).getServices().get(j).getId().equals(u.getService().getSecondary().get(i).getId())) {
                            myCareNames += u.getService().getSecondary().get(i).getName() + ", ";
                        }
                    }
                    for (int j = 0; j < allServices.get(2).getServices().size(); j++) {
                        //events
                        if (allServices.get(2).getServices().get(j).getId().equals(u.getService().getSecondary().get(i).getId())) {
                            eventsNames += u.getService().getSecondary().get(i).getName() + ", ";
                        }
                    }

                }

                if (!SingleInstance.getInstance().getPrimaryName().split("@")[0].equals("null")) {
                    tv_primary_lable.setText(SingleInstance.getInstance().getPrimaryName().split("@")[0]);
                } else {
                    tv_primary_lable.setText("0 services added");
                }
                tv_domestic_label.setText(domesticNames.substring(0, domesticNames.length() - 2));
                txt_mycare_counts.setText(myCareNames.substring(0, myCareNames.length() - 2));
                tv_events_label.setText(eventsNames.substring(0, eventsNames.length() - 2));
            } catch (Exception e) {
                Log.e(TAG, "onResume: first catch " + e.toString());
            }
        } else {
            try {
                if (isDomesticClicked) {
                    int domesticCounter = SingleInstance.getInstance().getDomesticCount();
                    tv_domestic_label.setText(domesticCounter + " services added");
                    isDomesticClicked = false;
                }

                if (isCareClicked) {
                    int myCareCounter = SingleInstance.getInstance().getMyCareCount();
                    txt_mycare_counts.setText(myCareCounter + " services added");
                    isCareClicked = false;
                }

                if (isEventsClicked) {
                    int eventsCounter = SingleInstance.getInstance().getEventsCount();
                    tv_events_label.setText(eventsCounter + " services added");
                    isEventsClicked = false;
                }

            } catch (Exception e) {
                tv_domestic_label.setText("N/A");
                txt_mycare_counts.setText("N/A");
                tv_events_label.setText("N/A");
                Log.e(TAG, "onResume: second catch " + e.toString());
            }


            if (!SingleInstance.getInstance().getPrimaryName().isEmpty()) {
                if (!SingleInstance.getInstance().getPrimaryName().split("@")[0].equals("Primary Service")) {
                    tv_primary_lable.setText(SingleInstance.getInstance().getPrimaryName().split("@")[0]);
                    Log.e(TAG, "onResume: " + SingleInstance.getInstance().getPrimaryName().split("@")[0]);
                    primaryServiceId = SingleInstance.getInstance().getPrimaryName().split("@")[1];
                } else {
                    tv_primary_lable.setText("0 services added");
                }
            }
        }
    }

    private void getAllServices() {
        allServices = MyApp.getApplication().readService();
    }


    private Context getContext() {
        return ChangeServicesActivity.this;
    }

    private void setupUiElement() {

        setTouchNClick(R.id.tv_domestic);
        setTouchNClick(R.id.tv_construction);
        setTouchNClick(R.id.tv_events);
        setTouchNClick(R.id.btn_continue);
        setTouchNClick(R.id.tv_primary);

        tv_primary_lable = findViewById(R.id.tv_primary_lable);

        tv_domestic_label = findViewById(R.id.tv_domestic_label);
        txt_mycare_counts = findViewById(R.id.txt_mycare_counts);
        tv_events_label = findViewById(R.id.tv_events_label);
        listdata = (ArrayList) DummyLocation.getListData();

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
            isDomesticClicked = true;
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 0) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ChangeServicesActivity.this, AddServicesActivity.class);
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
            Intent intent = new Intent(ChangeServicesActivity.this, AddServicesActivity.class);
            intent.putExtra("key", "Primary");
            intent.putExtra("isPrimary", true);
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(0));
            startActivity(intent);
        } else if (v.getId() == R.id.tv_construction) {
            isCareClicked = true;
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 1) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ChangeServicesActivity.this, AddServicesActivity.class);
            intent.putExtra("key", "construction");
            intent.putExtra("isCompany", isCompany);
            SingleInstance.getInstance().setSelectedServiceCategory(allServices.get(1));
            startActivity(intent);
        } else if (v.getId() == R.id.tv_events) {
            isEventsClicked = true;
            if (allServices == null) {
                getAllServices();
                return;
            }
            if (allServices.size() == 2) {
                MyApp.popMessage("Message", "No data available for this category", getContext());
                return;
            }
            Intent intent = new Intent(ChangeServicesActivity.this, AddServicesActivity.class);
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
        startActivity(new Intent(ChangeServicesActivity.this, MainActivity.class));
        finishAffinity();
    }


    private void updateProfileInformation() {
        if (SingleInstance.getInstance().getServicesId().equals(""))
            updateServices("");
        else
            updateServices(SingleInstance.getInstance().getServicesId().substring(0, SingleInstance.getInstance().getServicesId().length() - 1).replaceAll(" ", ""));
    }

    private void updateServices(String serviceids) {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        p.put("primaryservices", primaryServiceId);
        p.put("services", serviceids);
        postCall(getContext(), AppConstant.BASE_URL + "updateServices", p, "Updating...", 9);
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
        } else if (callNumber == 9) {

            MyApp.setSharedPrefString(AppConstant.PRIMARY_SERVICE_ID, primaryServiceId);
            SingleInstance.getInstance().setServicesId("");
            RequestParams p = new RequestParams();
            p.put("phone", MyApp.getApplication().readUser().getPhone());
            postCall(getContext(), AppConstant.BASE_URL + "login", p, "Updating...", 10);
        } else if (callNumber == 10) {
            User u = null;
            try {
                u = new Gson().fromJson(o.getJSONObject("data").toString(), User.class);
                MyApp.getApplication().writeUser(u);
                SingleInstance.getInstance().setPrimaryName("");
                SingleInstance.getInstance().setDomesticCount(0);
                SingleInstance.getInstance().setMyCareCount(0);
                SingleInstance.getInstance().setEventsCount(0);
                finish();
            } catch (JSONException e) {
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