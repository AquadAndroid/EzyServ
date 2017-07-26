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
import android.widget.Toast;

import com.ezyserv.custome.CustomActivity;

public class AddAddressActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText addres_one, address_two, pin;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Add Address");
        actionBar.setTitle("");

        setupuiElement();
    }


    private void setupuiElement() {

        setTouchNClick(R.id.btn_save_address);


        addres_one = (EditText) findViewById(R.id.edt_address_line_one);
        address_two = (EditText) findViewById(R.id.edt_address_line_two);
        pin = (EditText) findViewById(R.id.edt_address_pin);

        save = (Button) findViewById(R.id.btn_save_address);

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_save_address) {
          startActivity(new Intent(new Intent(AddAddressActivity.this, SavedAddressActivity.class)));
        }
    }

}
