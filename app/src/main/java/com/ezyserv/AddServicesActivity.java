package com.ezyserv;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;

public class AddServicesActivity extends CustomActivity {
    private Toolbar toolbar;
    private String value;
    private RecyclerView ServiceList;
    private Button Save;
    private AddServiceAdapter adapter;
    private Services services;
    String qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add domestic services");
        actionBar.setTitle("");
        Bundle extras = getIntent().getExtras();
        value = extras.getString("key").toString();


        if (value.equals("domestic")) {
            mTitle.setText("Add Domestic services");
        } else if (value.equals("construction")) {
            mTitle.setText("Add My Care services");
        } else if (value.equals("events")) {
            mTitle.setText("Add Events services");
        }

        ServiceList = (RecyclerView) findViewById(R.id.service_recycle);
        ServiceList.setLayoutManager(new LinearLayoutManager(this));
        services = SingleInstance.getInstance().getSelectedServiceCategory();
        adapter = new AddServiceAdapter(services, this);
        ServiceList.setAdapter(adapter);
        Save = (Button) findViewById(R.id.btn_save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty=adapter.value;
               // Toast.makeText(AddServicesActivity.this,""+qty, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("editTextValue",qty);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }


 /*   public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
          //  String ItemName = intent.getStringExtra("item");
           qty = intent.getStringExtra("quantity");

        }
    };*/
}
