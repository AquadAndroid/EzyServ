package com.ezyserv;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Country;
import com.ezyserv.utills.AppConstant;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class CustomerSignUpActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    private Toolbar toolbar;
    private EditText cust_name, cust_email, cust_phone;
    private CountryCodePicker cust_countryCodePicker;
    private TextView cust_tv_login, cust_tv_terms_cond;
    private CheckBox cust_checkBox;
    private Button cust_btn_sign_up;
    private LoginButton login_button;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setResponseListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up as Customer");
        actionBar.setTitle("");
        if (MyApp.getApplication().readCountry().size() == 0) {
            collectCountryData();
        }
        setupuiElement();
    }

    private void collectCountryData() {
        getCall(getContext(), AppConstant.BASE_URL + "getAllCountry", "Collecting Country data...", 1);
    }

    private void setupuiElement() {

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
        cust_countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        cust_btn_sign_up = (Button) findViewById(R.id.cust_btn_sign_up);

        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions("email,public_profile");
        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                MyApp.spinnerStart(getContext(), getString(R.string.loading));
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                MyApp.spinnerStop();
                                String fb_id = null;
                                try {
                                    fb_id = object.getString("id");
                                    MyApp.setSharedPrefString(AppConstant.FB_ID, fb_id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (MyApp.isConnectingToInternet(getContext())) {
                                        Log.d("Fb_info", object.getString("email") + "\n" + object.getString("name"));
//                                        registerUser(object.getString("email"), object.getString("name").split(" ")[0], object.getString("name").split(" ")[1]);
                                        LoginManager.getInstance().logOut();
                                        startActivity(new Intent(CustomerSignUpActivity.this, CustomerFacebookActivity.class)
                                                .putExtra("name", object.optString("name"))
                                                .putExtra("email", object.optString("email")));
                                    } else {
                                        MyApp.popMessage(getString(R.string.alert), getString(R.string.connect_working_internet), getContext());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    try {
                                        object.getString("email");
                                        Log.d("Fb_info", object.getString("email") + "\n" + object.getString("name"));
//                                        registerUser(object.getString("email"), object.getString("name").split(" ")[0], object.getString("name").split(" ")[0]);
                                        LoginManager.getInstance().logOut();
                                        startActivity(new Intent(CustomerSignUpActivity.this, CustomerFacebookActivity.class)
                                                .putExtra("name", object.optString("name"))
                                                .putExtra("email", object.optString("email")));
                                    } catch (JSONException ee) {

                                    }

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }

    private Context getContext() {
        return CustomerSignUpActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.customer_login) {
            startActivity(new Intent(CustomerSignUpActivity.this, CustomerLoginActivity.class));
        } else if (v.getId() == R.id.tv_term_con) {
            Toast.makeText(this, "Term Condition yet To be described ", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.cust_btn_sign_up) {
            if (!MyApp.isConnectingToInternet(getContext())) {
                MyApp.popMessage("Alert", "Please connect to working internet connection.", getContext());
                return;
            }
            if (TextUtils.isEmpty(cust_name.getText().toString())) {
                cust_name.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(cust_email.getText().toString())) {
                cust_email.setError("Enter Your email address");
                return;
            } else if (TextUtils.isEmpty(cust_phone.getText().toString())) {
                cust_phone.setError("Enter mobile number");
                return;
            } else if (!cust_checkBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
                return;
            }

            MyApp.setSharedPrefString("name", cust_name.getText().toString());
            MyApp.setSharedPrefString("email", cust_email.getText().toString());
            registerUser();

        }

    }

    private void registerUser() {
        phVerification();
    }

    private void phVerification() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);

        final String phone_no;
        phone_no = cust_countryCodePicker.getSelectedCountryCodeWithPlus() + " " + cust_phone.getText().toString();
        TextView verification_message = (TextView) dialog.findViewById(R.id.verification_message);
        verification_message.setText("A Verification code will be sent to " + phone_no + " for verification.");
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
                intent.putExtra("phone", phone_no);
                intent.putExtra("isRegister", true);
                startActivity(intent);
            }
        });
        dialog.show();

    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Country>>() {
            }.getType();
            try {
                List<Country> country = gson.fromJson(o.getJSONArray("data").toString(), listType);
                MyApp.getApplication().writeCountry(country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());
    }
}
