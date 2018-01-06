package com.ezyserv;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

public class PaymentActivityMain extends CustomActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_main);
        setUpToolBar();
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Add Money to Wallet");
        actionBar.setTitle("");

    }
}
