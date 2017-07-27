package com.ezyserv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

public class ServiceDeatailActivityOne extends CustomActivity {
    private Toolbar Dtoolbar;
    private Button btn_continue;
    private TextView verifyemail;
    private EditText demail;
    private EditText edt_service_name;
    private EditText edt_service_mobile;
    private String value="";
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_deatail_one);
        Dtoolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_title);
        TextView mCount = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_count);
        mTitle.setText("Add your details");
        mCount.setText("1/2");
        actionBar.setTitle("");
        demail = (EditText) findViewById(R.id.edt_service_detail_email);
        edt_service_name = (EditText) findViewById(R.id.edt_service_name);
        edt_service_mobile = (EditText) findViewById(R.id.edt_service_mobile);
        verifyemail = (TextView) findViewById(R.id.verify_email);
        btn_continue = (Button) findViewById(R.id.btn_serv_detail_sign_up);

        demail.setText(MyApp.getSharedPrefString("email"));
        edt_service_name.setText(MyApp.getSharedPrefString("name"));
        edt_service_mobile.setText(MyApp.getSharedPrefString("phone"));

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setCcpClickable(false);
        ccp.setCountryForPhoneCode(Integer.parseInt(edt_service_mobile.getText().toString().split(" ")[0].replace("+","")));

        value = getIntent().getStringExtra("key");

        if (value.equals("email_verification")) {
            verifyemail.setVisibility(View.GONE);
            demail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shape, 0);
        } else if(value.equals("phone_verified")) {
            verifyemail.setVisibility(View.VISIBLE);
            demail.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
            verifyemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ServiceDeatailActivityOne.this, ServiceEmailVerificationActivity.class));
                }
            });
        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceDeatailActivityOne.this, ServiceDetailActivityTwo.class));
            }
        });

    }
}
