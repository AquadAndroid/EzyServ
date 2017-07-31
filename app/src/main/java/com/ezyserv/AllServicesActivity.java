package com.ezyserv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

public class AllServicesActivity extends CustomActivity {
private Toolbar toolbar;
private RelativeLayout rel_domestic, rel_construction, rel_events;
    private TextView counter_domestic, counter_construction, counter_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);

        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("All Services");
        actionBar.setTitle("");
        setupuiElement();


    }

    private void setupuiElement() {

        setTouchNClick(R.id.rel_domestic);
        setTouchNClick(R.id.rel_construction);
        setTouchNClick(R.id.rel_events);


        rel_domestic=(RelativeLayout)findViewById(R.id.rel_domestic);
        rel_construction=(RelativeLayout)findViewById(R.id.rel_construction);
        rel_events=(RelativeLayout)findViewById(R.id.rel_events);

        counter_domestic=(TextView)findViewById(R.id.counter_domestic);
        counter_construction=(TextView)findViewById(R.id.counter_construction);
        counter_events=(TextView)findViewById(R.id.counter_events);

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rel_domestic) {
            startActivity(new Intent(new Intent(AllServicesActivity.this, DomesticActivity.class)));
        }else if(v.getId() == R.id.rel_domestic){

        }else if(v.getId() == R.id.rel_domestic){

        }
    }
}
