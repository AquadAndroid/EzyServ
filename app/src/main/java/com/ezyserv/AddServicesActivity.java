package com.ezyserv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Services;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddServicesActivity extends CustomActivity {
    private Toolbar toolbar;
    private String value;
    private RecyclerView ServiceList;
    private Button Save;
    private AddServiceAdapter adapter;
    private Services services;
    private boolean isPrimary;
    private boolean isCompany;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrimary = getIntent().getBooleanExtra("isPrimary", false);
        isCompany = getIntent().getBooleanExtra("isCompany", false);
        setContentView(R.layout.activity_add_services);
        services = SingleInstance.getInstance().getSelectedServiceCategory();
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
        } else {
            mTitle.setText("Add Primary service");
        }

        ServiceList = (RecyclerView) findViewById(R.id.service_recycle);
        ServiceList.setLayoutManager(new LinearLayoutManager(this));
        services = SingleInstance.getInstance().getSelectedServiceCategory();
        adapter = new AddServiceAdapter(services, this, isPrimary, isCompany);
        ServiceList.setAdapter(adapter);
        Save = (Button) findViewById(R.id.btn_save);

        if (isPrimary) {
            Save.setVisibility(View.GONE);
        }

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value.equals("domestic")) {
                    SingleInstance.getInstance().setDomesticCount(adapter.count);
                } else if (value.equals("construction")) {
                    SingleInstance.getInstance().setMyCareCount(adapter.count);
                } else if (value.equals("events")) {
                    SingleInstance.getInstance().setEventsCount(adapter.count);
                }
//7, 6, 5]16, 15, 14]18, 22]
                String ids = adapter.idMap.keySet().toString();
                ids = ids.substring(1, ids.length());
                ids = SingleInstance.getInstance().getServicesId() + ids;
                SingleInstance.getInstance().setServicesId(ids.replace("]", ","));
                finish();
            }
        });

    }

    private Context getContext() {
        return AddServicesActivity.this;
    }

    public void setPrimaryName(String primary, String id) {
        SingleInstance.getInstance().setPrimaryName(primary + "@" + id);
        finish();
    }
}
