package com.ezyserv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ezyserv.custome.CustomActivity;
import com.ezyserv.custome.CustomFragment;

import static android.R.attr.value;

public class PhoneVerificationActivity extends CustomActivity {
private Button Btn_verify;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Btn_verify=(Button)findViewById(R.id.btn_verify);
        Bundle extras = getIntent().getExtras();

             value = extras.getString("key").toString();

        Btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value.equals("customer_login") ){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "customer_login");
                    startActivity(intent);
                }else if(value.equals("customer_facebook")){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "customer_facebook");
                    startActivity(intent);
                }else if(value.equals("customer_signup")){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "customer_signup");
                    startActivity(intent);
                }else if(value.equals("service_facebook")){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "service_facebook");
                    startActivity(intent);
                }else if(value.equals("service_login")){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "service_login");
                    startActivity(intent);
                }else if(value.equals("service_signup")){
                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                    intent.putExtra("ezy", "service_signup");
                    startActivity(intent);
                }
            }
        });
    }
}
