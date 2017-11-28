package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;
import com.ezyserv.utills.AppConstant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class SplashActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    private static final int SPLASH_DURATION_MS = 1000;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setResponseListener(this);
        getServices();
        //  getHashKey();



    }

    private void getServices() {
        getCall(getContext(), AppConstant.BASE_URL + "getAllServices", "", 1);
    }


    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optString("status").equals("true")) {
                Type listType = new TypeToken<List<Services>>() {
                }.getType();
                try {
                    //  allServices = gson.fromJson(o.getJSONArray("data").toString(), listType);
                    List<Services> u = new Gson().fromJson(o.getJSONArray("data").toString(), listType);
                    MyApp.getApplication().writeService(u);
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyApp.popMessage("Alert!", "Parsing error.", getContext());
                    return;
                } catch (JsonSyntaxException ee) {
                }
            } else {
                MyApp.popMessageWithFinish("Alert!", o.optString("Message"), SplashActivity.this);
                return;
            }
            if (MyApp.getStatus(AppConstant.IS_LOGIN)) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getContext(), SignUpSelection.class);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.showMassage(getContext(), error);
    }
   /* private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ezyserv",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/

    private Context getContext() {
        return SplashActivity.this;
    }
}




