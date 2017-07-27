package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

public class ServiceFacebookActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText edt_service_name, edt_service_email, edt_service_mobile;
    private CountryCodePicker service_countryCodePicker;
    private Button Serv_btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_facebook);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up as Service Provider");
        mTitle.setTextSize(18);
        actionBar.setTitle("");

        setupuiElement();
    }

    private void setupuiElement() {

        setTouchNClick(R.id.serv_fb_signup);


        edt_service_name = (EditText) findViewById(R.id.edt_service_name);
        edt_service_email = (EditText) findViewById(R.id.edt_service_email);
        edt_service_mobile = (EditText) findViewById(R.id.edt_service_mobile);

        edt_service_name.setText(getIntent().getStringExtra("name"));
        edt_service_email.setText(getIntent().getStringExtra("email"));
        MyApp.setSharedPrefString("name", edt_service_name.getText().toString());
        MyApp.setSharedPrefString("email", edt_service_email.getText().toString());
        service_countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        Serv_btn_signup = (Button) findViewById(R.id.serv_fb_signup);


    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.serv_fb_signup) {
            if (TextUtils.isEmpty(edt_service_name.getText().toString())) {
                edt_service_name.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(edt_service_email.getText().toString())) {
                edt_service_email.setError("Enter Your email address");
                return;
            } else if (!MyApp.isEmailValid(edt_service_email.getText().toString())) {
                edt_service_email.setError("Enter a valid email address");
                return;
            } else if (TextUtils.isEmpty(edt_service_mobile.getText().toString())) {
                edt_service_mobile.setError("Enter mobile number");
                return;
            }
            phVerification();

        }
    }


    private void phVerification() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        final String phone_no = service_countryCodePicker.getSelectedCountryCodeWithPlus() + " " + edt_service_mobile.getText().toString();
        TextView verification_message = (TextView) dialog.findViewById(R.id.verification_message);
        verification_message.setText("A Verification code will be sent to "+phone_no+" for verification.");
        Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.ph_verify_cancel);
        Button dialog_send_Button = (Button) dialog.findViewById(R.id.btn_send);
        // if button is clicked, close the custom dialog
        dialog_cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog_send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceFacebookActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "service_facebook");
                intent.putExtra("phone", phone_no);
                startActivity(intent);
            }
        });
        dialog.show();

    }


}
