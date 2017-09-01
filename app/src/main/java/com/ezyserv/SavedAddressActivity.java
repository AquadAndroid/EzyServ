package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.DummyData;
import com.ezyserv.adapter.DummySavedAddressData;
import com.ezyserv.adapter.SavedAddressAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class SavedAddressActivity extends CustomActivity {
    private Toolbar toolbar;
    private RecyclerView savedAddress;
    private TextView addNewAddress;
    private Button btnContinue;
    private SavedAddressAdapter adapter;
    private ArrayList listdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Select an address");
        actionBar.setTitle("");

        setupuiElement();
    }


    private void setupuiElement() {


        setTouchNClick(R.id.tv_add_new_add);
        setTouchNClick(R.id.btn_continue);


        addNewAddress = (TextView) findViewById(R.id.tv_add_new_add);

        btnContinue = (Button) findViewById(R.id.btn_continue);
        savedAddress = (RecyclerView) findViewById(R.id.recy_saved_address);

        listdata = (ArrayList) DummySavedAddressData.getListData();

        savedAddress.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SavedAddressAdapter(listdata, this);
        savedAddress.setAdapter(adapter);

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_add_new_add) {
            startActivity(new Intent(SavedAddressActivity.this, AddAddressActivity.class));
        } else if (v.getId() == R.id.btn_continue) {

        }
    }


}
