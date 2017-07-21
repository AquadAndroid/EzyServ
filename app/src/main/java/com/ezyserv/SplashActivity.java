package com.ezyserv;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ezyserv.custome.CustomActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends CustomActivity {
    private static final int SPLASH_DURATION_MS = 500;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
     //  getApiData();



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, SignUpSelection.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_DURATION_MS);


    }



/*    private void getApiData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
       client.post("http://stubuz.com/floterapi/index.php/carapi/getratecard", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject o) {
                if (o.optString("status").equals("OK")) {
                   // RateCard u = new Gson().fromJson(o.toString(), RateCard.class);
                    //SingleInstance.getInstance().setRateCard(u);
                    mHandler.postDelayed(mEndSplash, SPLASH_DURATION_MS);
                } else {
                    AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                    b.setTitle("Not Connected?").setMessage("Please check your internet connection and try again");
                    b.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int i) {
                            getApiData();
                            d.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int i) {
                            d.dismiss();
                            finish();
                        }
                    }).create().show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                b.setTitle("Not Connected?").setMessage("Please check your internet connection and try again");
                b.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        getApiData();
                        d.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        d.dismiss();
                        finish();
                    }
                }).create().show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
                b.setTitle("Not Connected?").setMessage("Please check your internet connection and try again");
                b.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        getApiData();
                        d.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        d.dismiss();
                        finish();
                    }
                }).create().show();
            }
        });
}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mEndSplash.run();
        return super.onTouchEvent(event);
    }

    private Runnable mEndSplash = new Runnable() {
        public void run() {
            if (!isFinishing()) {
                mHandler.removeCallbacks(this);

                startActivity(new Intent(
                        SplashActivity.this, MainActivity.class
                ));

                finish();
            }
        }

        ;
    };*/
    }




