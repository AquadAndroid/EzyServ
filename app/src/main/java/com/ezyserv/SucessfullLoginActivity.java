package com.ezyserv;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

public class SucessfullLoginActivity extends CustomActivity {
    private static final int SPLASH_DURATION_MS = 1000;
    private Handler mHandler = new Handler();
    private TextView tv_sucessfull, tv_sucessfull_label;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucessfull_login);
        tv_sucessfull=(TextView)findViewById(R.id.label_sucessfull);
        tv_sucessfull_label=(TextView)findViewById(R.id.sucessfull_label);

        Bundle extras = getIntent().getExtras();

        value = extras.getString("ezy").toString();


        if(value.equals("customer_login") ){
            tv_sucessfull.setText("Log In successful !");
            tv_sucessfull_label.setText("Taking you to Home");
            new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, MainActivity.class);
                    startActivity(i);


                    finish();
                }
            }, SPLASH_DURATION_MS);

        }else if(value.equals("customer_facebook")){
            tv_sucessfull.setText("Successfully Verified !");
            tv_sucessfull_label.setText("Taking you to Home");

            new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, MainActivity.class);
                    startActivity(i);


                    finish();
                }
            }, SPLASH_DURATION_MS);
        }else if(value.equals("customer_signup")){
            tv_sucessfull.setText("Successfully Verified !");
            tv_sucessfull_label.setText("Taking you to Home");

            new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, MainActivity.class);
                    startActivity(i);


                    finish();
                }
            }, SPLASH_DURATION_MS);
        }else if(value.equals("service_facebook")){
            tv_sucessfull.setText("Sign Up Successful !");
            tv_sucessfull_label.setText("Taking you to Home");

            new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, MainActivity.class);
                    startActivity(i);


                    finish();
                }
            }, SPLASH_DURATION_MS);
        }else if(value.equals("service_login")){
            tv_sucessfull.setText("Log In successful");
            tv_sucessfull_label.setText("Taking you to Home");

            new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, MainActivity.class);
                    startActivity(i);


                    finish();
                }
            }, SPLASH_DURATION_MS);
        }else if(value.equals("service_signup")){
            tv_sucessfull.setText("Successfully Verified !");
            tv_sucessfull_label.setText("Taking you to next steps");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SucessfullLoginActivity.this, ServiceDeatailActivityOne.class);
                    i.putExtra("key", "phone_verified");
                    startActivity(i);


                }
            }, SPLASH_DURATION_MS);


        }



    }
}
