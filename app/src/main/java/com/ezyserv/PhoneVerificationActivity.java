package com.ezyserv;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends CustomActivity {
    private Button Btn_verify;
    private String value;
    private TextView mb_no;
    private TextView txt_counter;
    private EditText edt_otp;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Btn_verify = (Button) findViewById(R.id.btn_verify);
        mb_no = (TextView) findViewById(R.id.mb_no);
        mb_no.setText(getIntent().getStringExtra("phone"));
        Bundle extras = getIntent().getExtras();

        MyApp.getApplication().setSharedPrefString("phone", mb_no.getText().toString());
        value = extras.getString("key").toString();

        txt_counter = (TextView) findViewById(R.id.txt_counter);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        showCounter();

        Btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                intent.putExtra("ezy", value);
                startActivity(intent);
            }
        });
    }

    private void showCounter() {

        CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be called every second.
                txt_counter.setText(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + ":" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                txt_counter.setText("00:00");
                //you are good to go.
                //30 seconds passed.
            }
        };
        mCountDownTimer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.GONE);
                edt_otp.setText("123456");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                intent.putExtra("ezy", value);
                startActivity(intent);
            }
        }, 1500);
    }

}
