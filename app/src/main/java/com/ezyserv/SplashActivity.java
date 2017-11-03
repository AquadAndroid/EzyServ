package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SplashActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    private static final int SPLASH_DURATION_MS = 1000;
    private Handler mHandler = new Handler();
   // private List<Services> allServices = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setResponseListener(this);

      //  getHashKey();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
              /*  if (MyApp.getStatus(AppConstant.IS_LOGIN)) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, SignUpSelection.class);
                    startActivity(i);
                    finish();
                }
*/
                getAllServices();
              //getServices();
            }
        }, SPLASH_DURATION_MS);


    }

    private void getAllServices() {
        getCall(getContext(), AppConstant.BASE_URL + "getAllServices", "Loading services...", 1);
    }

   /* private void getServices(){
        RequestParams p = new RequestParams();
        p.put("services","");
        p.put("current_lat","78.2");
        p.put("current_lng","24.2");
        p.put("radius","1000000");

        postCall(getContext(),AppConstant.BASE_URL+"nearByServiceProvider", p,"Getting....",1);
    }*/



    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
if (callNumber == 1){
    if(o.optString("status").equals("true")){
        Type listType = new TypeToken<List<Services>>() {
        }.getType();
        try{
         //  allServices = gson.fromJson(o.getJSONArray("data").toString(), listType);
            List<Services> u = new Gson().fromJson(o.getJSONArray("data").toString(), listType);
            MyApp.getApplication().writeService(u);
        }catch (JSONException e){
            e.printStackTrace();
            MyApp.popMessage("Alert!", "Parsing error.", getContext());
            return;
        }catch (JsonSyntaxException ee){

        }
    }else {
        MyApp.popMessageWithFinish("Alert!",o.optString("Message"),SplashActivity.this);
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

   private Context getContext(){return SplashActivity.this;}
    }




