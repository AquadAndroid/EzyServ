package com.ezyserv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.DummyData;
import com.ezyserv.adapter.DummyListItem;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class AddServicesActivity extends CustomActivity {
private Toolbar toolbar;
    private String value;
    private RecyclerView ServiceList;
    private Button Save;
    private AddServiceAdapter adapter;
    private ArrayList listdata;
    ArrayList<DummyListItem> dum900000000000000000000000myListItems;
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
        if(value.equals("domestic")){
            mTitle.setText("Add domestic services");
        }else if (value.equals("construction")){
            mTitle.setText("Add Construction services");
        }else if (value.equals("events")){
            mTitle.setText("Add Events services");
        }

        listdata = (ArrayList) DummyData.getListData();
        ServiceList=(RecyclerView)findViewById(R.id.service_recycle);
        ServiceList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AddServiceAdapter(listdata, this);
        ServiceList.setAdapter(adapter);
       Save=(Button)findViewById(R.id.btn_save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddServicesActivity.this, ServiceDetailActivityTwo.class));
            }
        });

    }
}
