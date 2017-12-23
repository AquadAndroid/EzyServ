package com.ezyserv;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;

import java.util.Calendar;

public class ScheduleServiceActivity extends CustomActivity {
    private Toolbar toolbar;
    private Button btn_continue;
    private RadioButton slot1, slot2, slot3, slot4;
    private TextView addNewAddress, selectedDate, selectDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_service);
        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Schedule your service");
        actionBar.setTitle("");
        setupuiElement();

    }


    private void setupuiElement() {

        setTouchNClick(R.id.tv_select_date);
        setTouchNClick(R.id.tv_add_new_add);
        setTouchNClick(R.id.btn_continue);


        addNewAddress = (TextView) findViewById(R.id.edt_address_line_one);
        selectedDate = (TextView) findViewById(R.id.tv_selected_date);
        selectDate = (TextView) findViewById(R.id.tv_select_date);
        addNewAddress = (TextView) findViewById(R.id.tv_add_new_add);

        btn_continue = (Button) findViewById(R.id.btn_continue);


    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_select_date) {
            dateDialog();
        } else if (v.getId() == R.id.tv_add_new_add) {
            startActivity(new Intent(new Intent(ScheduleServiceActivity.this, AddAddressActivity.class)));
        } else if (v.getId() == R.id.btn_continue) {
            startActivity(new Intent(new Intent(ScheduleServiceActivity.this, MainActivity.class)));
        }
    }


    public void dateDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        selectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
