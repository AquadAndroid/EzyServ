package com.ezyserv;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.DummyData;
import com.ezyserv.adapter.DummyPaintingData;
import com.ezyserv.adapter.DummyPaintingItem;
import com.ezyserv.adapter.PaintingAdapter;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class Painting extends CustomActivity {
    private Toolbar toolbar;
    private RecyclerView domestic_service_item;
    private FloatingActionButton filter;
    private PaintingAdapter adapter;
    private ArrayList listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);
        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Painting");
        actionBar.setTitle("");

        setupuiElement();

    }


    private void setupuiElement() {

        setTouchNClick(R.id.float_filter);
        filter=(FloatingActionButton)findViewById(R.id.float_filter);

        domestic_service_item = (RecyclerView) findViewById(R.id.recyclerview_painting);

        listdata = (ArrayList) DummyPaintingData.getListData();

        domestic_service_item.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PaintingAdapter(listdata, this);
        domestic_service_item.setAdapter(adapter);
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.float_filter) {
            startActivity(new Intent(new Intent(Painting.this, FiltersActivity.class)));
        }
    }

}
