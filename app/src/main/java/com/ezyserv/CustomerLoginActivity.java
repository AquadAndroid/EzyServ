package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
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

public class CustomerLoginActivity extends CustomActivity {

    private Toolbar toolbar;
    private EditText cust_mobile_no;
    private Button cust_login;
    private CheckBox checkBox;
    private CountryCodePicker countryCodePicker;
    private TextView cust_term_cond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Log In as Customer");
        actionBar.setTitle("");
        setupuiElement();

       /* cust_mobile_no = (EditText)findViewById(R.id.custom_mb_no);
        cust_login=(Button)findViewById(R.id.custom_login);
        checkBox=(CheckBox)findViewById(R.id.terms_condition_checkbox);
        countryCodePicker=(CountryCodePicker)findViewById(R.id.ccp);
        cust_term_cond=(TextView)findViewById(R.id.custom_term_cond);*/

      /*  cust_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(cust_mobile_no).cus)
                startActivity(new Intent(CustomerLoginActivity.this, PhoneVerificationActivity.class));
            }
        });*/

    }

    private void setupuiElement() {

        setTouchNClick(R.id.custom_login);
        setTouchNClick(R.id.custom_term_cond);

        cust_mobile_no = (EditText) findViewById(R.id.custom_mb_no);
        cust_login = (Button) findViewById(R.id.custom_login);
        checkBox = (CheckBox) findViewById(R.id.terms_condition_checkbox);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        cust_term_cond = (TextView) findViewById(R.id.custom_term_cond);
        String htmlString="<u>terms and condition</u>";
        cust_term_cond.setText(Html.fromHtml(htmlString));
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.custom_login) {
            if (TextUtils.isEmpty(cust_mobile_no.getText().toString())) {
                cust_mobile_no.setError("Enter Mobile No");
                return;
            } else if (!checkBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
            }
            phVerification();

        } else if (v.getId() == R.id.custom_term_cond) {
            //Toast.makeText(this, "Terms and Condition", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(CustomerLoginActivity.this, MainActivity.class));
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
                Intent intent= new Intent(CustomerLoginActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "customer_login");
                startActivity(intent);
            }
        });
        dialog.show();

    }

}
