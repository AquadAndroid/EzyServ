package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

public class ServiceFacebookActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText Sname, Semail, Sphone;
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


        Sname = (EditText) findViewById(R.id.edt_service_name);
        Semail = (EditText) findViewById(R.id.edt_service_email);
        Sphone = (EditText) findViewById(R.id.edt_service_mobile);

        service_countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        Serv_btn_signup = (Button) findViewById(R.id.serv_fb_signup);


    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.serv_fb_signup) {
            if (TextUtils.isEmpty(Sname.getText().toString())) {
                Sname.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(Semail.getText().toString())) {
                Semail.setError("Enter Your email address");
            } else if (TextUtils.isEmpty(Sphone.getText().toString())) {
                Sphone.setError("Enter mobile number");
            }
            phVerification();

        }
    }


    private void phVerification() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        String phone_no;
        phone_no = service_countryCodePicker.getSelectedCountryCodeWithPlus() + " " + Sphone.getText().toString();
        TextView dialog_mb_no = (TextView) dialog.findViewById(R.id.tv_mb_no);
        dialog_mb_no.setText(phone_no);
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
                startActivity(intent);
            }
        });
        dialog.show();

    }


}
