package com.ezyserv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

public class ServiceDeatailActivityOne extends CustomActivity {
    private Toolbar Dtoolbar;
    private Button btn_continue;
    private TextView verifyemail;
    private EditText demail;
    private String value="";

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
        verifyemail = (TextView) findViewById(R.id.verify_email);
        btn_continue = (Button) findViewById(R.id.btn_serv_detail_sign_up);

        Bundle extras = getIntent().getExtras();

        value = extras.getString("key").toString();


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
