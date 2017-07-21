package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

public class ServiceLoginActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText cust_mobile_no;
    private Button cust_login;
    private CheckBox checkBox;
    private CountryCodePicker countryCodePicker;
    private TextView cust_term_cond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Log In as Service Provider");
        actionBar.setTitle("");

        setupuiElement();
    }

    private void setupuiElement() {


        setTouchNClick(R.id.service_btn_login);
        setTouchNClick(R.id.service_terms_condition);

        cust_mobile_no = (EditText) findViewById(R.id.service_mb_no);
        cust_login = (Button) findViewById(R.id.service_btn_login);
        checkBox = (CheckBox) findViewById(R.id.service_terms_condition_checkbox);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        cust_term_cond = (TextView) findViewById(R.id.service_terms_condition);
        String htmlString="<u>terms and condition</u>";
        cust_term_cond.setText(Html.fromHtml(htmlString));
    }


    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.service_btn_login) {
            if (TextUtils.isEmpty(cust_mobile_no.getText().toString())) {
                cust_mobile_no.setError("Enter Mobile No");
                return;
            } else if (!checkBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
            }


            phVerification();

        } else if (v.getId() == R.id.custom_term_cond) {
            Toast.makeText(this, "Terms and Condition", Toast.LENGTH_SHORT).show();
        }
    }






    private void phVerification(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        String phone_no;
        phone_no=countryCodePicker.getSelectedCountryCodeWithPlus()+" "+ cust_mobile_no.getText().toString();
        TextView dialog_mb_no=(TextView)dialog.findViewById(R.id.tv_mb_no);
        dialog_mb_no.setText(phone_no);
        Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.ph_verify_cancel);
        Button dialog_send_Button = (Button) dialog.findViewById(R.id.btn_send);
        // if button is clicked, close the custom dialog
        dialog_cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog_send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceLoginActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "service_login");
                startActivity(intent);
            }
        });
        dialog.show();

    }








}
