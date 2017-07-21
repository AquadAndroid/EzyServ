package com.ezyserv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

public class SignUpSelection extends CustomActivity {
private Toolbar toolbar;
    private Button customer_signup, service_sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up");
        actionBar.setTitle("");
        customer_signup=(Button)findViewById(R.id.btn_sign_up_cust);
        service_sign_up=(Button)findViewById(R.id.btn_sign_up_service);
        customer_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpSelection.this, CustomerSignUpActivity.class));
            }
        });

        service_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( SignUpSelection.this, ServiceSignUpActivity.class));
            }
        });
    }
}
