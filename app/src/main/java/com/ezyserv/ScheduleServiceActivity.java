package com.ezyserv;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.custome.CustomActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ScheduleServiceActivity extends CustomActivity {
    String TAG = "ScheduleServiceActivity";

    private Button btn_continue;
    private RadioGroup rd_grp_time_slot;
    private RadioButton rd_time_slot;
    private TextView tv_add_new_add, tv_selected_date, tv_select_date;
    private int mYear, mMonth, mDay;
    String date_to_be_sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_service);
        initActionBar();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Schedule your service");
        actionBar.setTitle("");
        setupuiElement();


        DateFormat dateFormat = new SimpleDateFormat("d MMM, ''yy");
        DateFormat dateFormatToBeSend = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_selected_date.setText(dateFormat.format(date));
        date_to_be_sent = dateFormatToBeSend.format(date);
    }


    private void setupuiElement() {
        setTouchNClick(R.id.tv_select_date);
        setTouchNClick(R.id.tv_add_new_add);
        setTouchNClick(R.id.btn_continue);


        tv_add_new_add = findViewById(R.id.edt_address_line_one);
        tv_selected_date = findViewById(R.id.tv_selected_date);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_add_new_add = findViewById(R.id.tv_add_new_add);
        btn_continue = findViewById(R.id.btn_continue);
        rd_grp_time_slot = findViewById(R.id.rd_grp_time_slot);
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_select_date) {
            dateDialog();
        } else if (v.getId() == R.id.tv_add_new_add) {
            startActivity(new Intent(new Intent(ScheduleServiceActivity.this, AddAddressActivity.class)));
        } else if (v.getId() == R.id.btn_continue) {
            if (rd_grp_time_slot.getCheckedRadioButtonId() >= 0) {

                String date = tv_selected_date.getText().toString();
                int selectedId = rd_grp_time_slot.getCheckedRadioButtonId();
                rd_time_slot = findViewById(selectedId);

                Intent intent = new Intent();
                intent.putExtra("service_start_date", date_to_be_sent);
                intent.putExtra("service_start_time", rd_time_slot.getText().toString());
                setResult(222, intent);
                finish();
            } else {
                Toast.makeText(this, "Please Select Time Slot !", Toast.LENGTH_SHORT).show();
            }

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
                        tv_selected_date.setText(parseDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                        date_to_be_sent = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public String parseDate(String time) {
        Log.e(TAG, "parseDateToHHMM: " + time);
        String inputPattern = "DD-M-yyyy";
        String outputPattern = "d MMM, ''yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
