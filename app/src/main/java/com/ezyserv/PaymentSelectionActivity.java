package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.DummyCode;
import com.ezyserv.adapter.DummyData;
import com.ezyserv.adapter.DummyListItem;
import com.ezyserv.adapter.DummyPrmoList;
import com.ezyserv.adapter.PromoCodeAdapter;
import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

public class PaymentSelectionActivity extends CustomActivity {
    private Toolbar toolbar;
    private RecyclerView coupons_recy;
    private RadioButton cash, wallet;
    private EditText promocode;
    private TextView wallet_money, recharge_wallet, remove_coupon;
    private Button done;
    private PromoCodeAdapter adapter;
    private ArrayList listdata;
    private ArrayList<DummyPrmoList> dummyListItems;
    private int isShedule = 0;
    private RelativeLayout rl_cash, rl_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);
        isShedule = getIntent().getIntExtra("isShedule", 0);
        toolbar = findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Select Payment Method");
        actionBar.setTitle("");

        setupuiElement();
        cash.setChecked(true);


        cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cash.isChecked()) {
                    wallet.setChecked(false);
                }
            }
        });

        wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (wallet.isChecked()) {
                    cash.setChecked(false);
                }
            }
        });
    }


    private void setupuiElement() {

        setTouchNClick(R.id.btn_rec_wallet);
        setTouchNClick(R.id.btn_remove_coupon);
        setTouchNClick(R.id.btn_done);


        cash = findViewById(R.id.rb_cash);
        wallet = findViewById(R.id.rb_wallet);

        promocode = findViewById(R.id.edt_promocode);
        rl_cash = findViewById(R.id.rl_cash);
        rl_wallet = findViewById(R.id.rl_wallet);

        if (isShedule == 0) {
            rl_cash.setVisibility(View.GONE);
            rl_wallet.setVisibility(View.VISIBLE);
        } else {
            rl_cash.setVisibility(View.VISIBLE);
            rl_wallet.setVisibility(View.GONE);
        }

        wallet_money = findViewById(R.id.tv_wallet_money);

        recharge_wallet = findViewById(R.id.btn_rec_wallet);
        remove_coupon = findViewById(R.id.btn_remove_coupon);
        done = findViewById(R.id.btn_done);


        coupons_recy = findViewById(R.id.recy_coupons);
        listdata = (ArrayList) DummyCode.getListData();
        coupons_recy.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PromoCodeAdapter(listdata, this);
        coupons_recy.setAdapter(adapter);


    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_rec_wallet) {
            startActivity(new Intent(PaymentSelectionActivity.this, AddMoneyActivity.class));
        } else if (v.getId() == R.id.btn_remove_coupon) {
            Toast.makeText(this, "Coupon Removed", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_done) {
            if (isShedule == 1) {
                startActivity(new Intent(PaymentSelectionActivity.this, ChatActivity.class));
                return;
            }
            promoTerms();
        }
    }


    private void promoTerms() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.promo_dialog);
        TextView promocode, terms_cond;
        Button ok;
        promocode = (TextView) dialog.findViewById(R.id.tv_promo_code);
        terms_cond = (TextView) dialog.findViewById(R.id.tv_terms);
        ok = (Button) dialog.findViewById(R.id.btn_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentSelectionActivity.this, AddMoneyActivity.class));
                finish();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);


        dialog.show();

    }


}
