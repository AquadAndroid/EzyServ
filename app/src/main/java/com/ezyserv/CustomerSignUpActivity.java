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

public class CustomerSignUpActivity extends CustomActivity {
    private Toolbar toolbar;
    private EditText cust_name, cust_email, cust_phone;
    private CountryCodePicker cust_countryCodePicker;
    private TextView cust_tv_login, cust_tv_terms_cond;
    private Button cust_signup_btn, cust_fb_btn;
    private CheckBox cust_checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up as Customer");
        actionBar.setTitle("");

        setupuiElement();





    }

    private void setupuiElement() {

        setTouchNClick(R.id.btn_fb_customer);
        setTouchNClick(R.id.customer_login);
        setTouchNClick(R.id.tv_term_con);
        setTouchNClick(R.id.cust_btn_sign_up);


        cust_name = (EditText) findViewById(R.id.edt_customer_name);
        cust_email = (EditText) findViewById(R.id.edt_customer_email);
        cust_phone = (EditText) findViewById(R.id.edt_customer_mobile);


        cust_tv_login = (TextView) findViewById(R.id.customer_login);
        cust_tv_terms_cond = (TextView) findViewById(R.id.tv_term_con);
        String htmlString = "<u>terms and condition</u>";
        cust_tv_terms_cond.setText(Html.fromHtml(htmlString));

        cust_checkBox = (CheckBox) findViewById(R.id.cust_terms_condition_checkbox);
        cust_countryCodePicker=(CountryCodePicker)findViewById(R.id.ccp);
        cust_fb_btn = (Button) findViewById(R.id.btn_fb_customer);
        cust_signup_btn = (Button) findViewById(R.id.cust_btn_sign_up);


    }

    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btn_fb_customer){
            startActivity(new Intent(CustomerSignUpActivity.this, CustomerFacebookActivity.class));
        }else if(v.getId()== R.id.customer_login){
            startActivity(new Intent(CustomerSignUpActivity.this, CustomerLoginActivity.class));
        }else if(v.getId()== R.id.tv_term_con){
            Toast.makeText(this, "Term Condition yet To be described ", Toast.LENGTH_SHORT).show();
        }else if(v.getId()== R.id.cust_btn_sign_up){
            if (TextUtils.isEmpty(cust_name.getText().toString())) {
                cust_name.setError("Enter your Name");
                return;
            }else if(TextUtils.isEmpty(cust_email.getText().toString())){
                cust_email.setError("Enter Your email address");
            }else if(TextUtils.isEmpty(cust_phone.getText().toString())){
                cust_phone.setError("Enter mobile number");
            } else if (!cust_checkBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
            }
            phVerification();

        }

    }






    private void phVerification(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        String phone_no;
        phone_no=cust_countryCodePicker.getSelectedCountryCodeWithPlus()+" "+ cust_phone.getText().toString();
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
                Intent intent = new Intent(CustomerSignUpActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "customer_signup");
                startActivity(intent);
            }
        });
        dialog.show();

    }






}
