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

import static com.ezyserv.R.dimen.dim_10;

public class ServiceSignUpActivity extends CustomActivity {

    private Toolbar toolbar;
    private EditText Sname, Semail, Sphone;
    private TextView serv_tv_login, serv_tv_terms_cond;
    private CheckBox servcheckBox;
    private Button service_btn_fb, service_btn_signup;
    private CountryCodePicker serv_countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicesign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up as Service Provider");
        mTitle.setTextSize(18);
        actionBar.setTitle("");
        setupuiElement();




/*
        Sname=(EditText)findViewById(R.id.edt_service_name);
        Semail=(EditText)findViewById(R.id.edt_service_email);
        Sphone=(EditText)findViewById(R.id.edt_service_mobile);


        serv_tv_login=(TextView)findViewById(R.id.service_login);
        serv_tv_terms_cond=(TextView)findViewById(R.id.tv_term_con_service);

        String htmlString="<u>terms and condition</u>";
        serv_tv_terms_cond.setText(Html.fromHtml(htmlString));
        servcheckBox=(CheckBox)findViewById(R.id.service_signup_checkbox);

        serv_tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( ServiceSignUpActivity.this, ServiceLoginActivity.class));

            }
        });*/
    }


    private void setupuiElement() {

        setTouchNClick(R.id.service_btn_fb);
        setTouchNClick(R.id.service_login);
        setTouchNClick(R.id.tv_term_con_service);
        setTouchNClick(R.id.btn_serv_sign_up);


        Sname = (EditText) findViewById(R.id.edt_service_name);
        Semail = (EditText) findViewById(R.id.edt_service_email);
        Sphone = (EditText) findViewById(R.id.edt_service_mobile);


        serv_countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        serv_tv_login = (TextView) findViewById(R.id.service_login);
        serv_tv_terms_cond = (TextView) findViewById(R.id.tv_term_con_service);

        String htmlString = "<u>terms and condition</u>";
        serv_tv_terms_cond.setText(Html.fromHtml(htmlString));
        servcheckBox = (CheckBox) findViewById(R.id.service_signup_checkbox);

        service_btn_fb = (Button) findViewById(R.id.service_btn_fb);
        service_btn_signup = (Button) findViewById(R.id.btn_serv_sign_up);


    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.service_btn_fb) {
            startActivity(new Intent(ServiceSignUpActivity.this, ServiceFacebookActivity.class));
        } else if (v.getId() == R.id.service_login) {
            startActivity(new Intent(ServiceSignUpActivity.this, ServiceLoginActivity.class));

        } else if (v.getId() == R.id.tv_term_con_service) {
            Toast.makeText(this, "Term Condition yet To be described ", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_serv_sign_up) {
            if (TextUtils.isEmpty(Sname.getText().toString())) {
                Sname.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(Semail.getText().toString())) {
                Semail.setError("Enter Your email address");
            } else if (TextUtils.isEmpty(Sphone.getText().toString())) {
                Sphone.setError("Enter mobile number");
            } else if (!servcheckBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
            }
            phVerification();

        }
    }


    private void phVerification() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        String phone_no;
        phone_no = serv_countryCodePicker.getSelectedCountryCodeWithPlus() + " " + Sphone.getText().toString();
        TextView dialog_mb_no = (TextView) dialog.findViewById(R.id.tv_mb_no);
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
                Intent intent = new Intent(ServiceSignUpActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "service_signup");
                startActivity(intent);
            }
        });
        dialog.show();

    }


}
