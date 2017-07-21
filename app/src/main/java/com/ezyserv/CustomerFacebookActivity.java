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

public class CustomerFacebookActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText cust_name, cust_email, cust_phone;
    private CountryCodePicker cust_countryCodePicker;
    private Button cust_signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_facebook);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up as Customer");
        actionBar.setTitle("");

        setupuiElement();
    }


    private void setupuiElement() {

        setTouchNClick(R.id.cust_fb_signup);


        cust_name = (EditText) findViewById(R.id.edt_customer_name);
        cust_email = (EditText) findViewById(R.id.edt_customer_email);
        cust_phone = (EditText) findViewById(R.id.edt_customer_mobile);


        cust_signup_btn = (Button) findViewById(R.id.cust_fb_signup);
        cust_countryCodePicker=(CountryCodePicker)findViewById(R.id.ccp);

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cust_fb_signup) {
            if (TextUtils.isEmpty(cust_name.getText().toString())) {
                cust_name.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(cust_email.getText().toString())) {
                cust_email.setError("Enter Your email address");
            } else if (TextUtils.isEmpty(cust_phone.getText().toString())) {
                cust_phone.setError("Enter mobile number");
            }

            phVerification();

        }
    }





    private void phVerification(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        String phone_no;
        phone_no=cust_countryCodePicker.getSelectedCountryCodeWithPlus()+" "+ cust_phone.getText().toString();
        TextView dialog_mb_no=(TextView)dialog.findViewById(R.id.tv_mb_no);
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
                Intent intent = new Intent(CustomerFacebookActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "customer_facebook");
                startActivity(intent);
            }
        });
        dialog.show();

    }
}
