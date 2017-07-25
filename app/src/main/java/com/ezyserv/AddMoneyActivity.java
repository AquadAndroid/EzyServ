package com.ezyserv;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.DummyCode;
import com.ezyserv.adapter.PromoCodeAdapter;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class AddMoneyActivity extends CustomActivity {
    private Toolbar toolbar;
    private TextView tv_three, tv_five, tv_ten;
    private EditText edt_amount;
    private Button done;
    private int value = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Recharge Wallet");
        actionBar.setTitle("");

        setupuiElement();
    }


    private void setupuiElement() {

        setTouchNClick(R.id.money_three_hundred);
        setTouchNClick(R.id.money_five_hundred);
        setTouchNClick(R.id.money_thousand);
        setTouchNClick(R.id.btn_done);


        edt_amount = (EditText) findViewById(R.id.edt_add_money);
        edt_amount.setText("" + 0);
        tv_three = (TextView) findViewById(R.id.money_three_hundred);
        tv_five = (TextView) findViewById(R.id.money_five_hundred);
        tv_ten = (TextView) findViewById(R.id.money_thousand);
        done = (Button) findViewById(R.id.btn_done);

    }


    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.money_three_hundred) {
            value = value + 300;
            edt_amount.setText("" + value);
        } else if (v.getId() == R.id.money_five_hundred) {
            value = value +500;
            edt_amount.setText("" + value);
        } else if (v.getId() == R.id.money_thousand) {
            value = value + 1000;
            edt_amount.setText("" + value);
        } else if (v.getId() == R.id.btn_done) {
            Toast.makeText(this, "Money Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
