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
import com.ezyserv.utills.AppConstant;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceSignUpActivity extends CustomActivity {

    private Toolbar toolbar;
    private EditText Sname, Semail, Sphone;
    private TextView serv_tv_login, serv_tv_terms_cond;
    private CheckBox servcheckBox;
    private Button service_btn_signup;
    private CountryCodePicker serv_countryCodePicker;
    private LoginButton login_button;
    private CallbackManager callbackManager;

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
    }


    private void setupuiElement() {

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

        service_btn_signup = (Button) findViewById(R.id.btn_serv_sign_up);

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
                                        startActivity(new Intent(getContext(), ServiceFacebookActivity.class)
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
                                        startActivity(new Intent(getContext(), ServiceFacebookActivity.class)
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
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private Context getContext() {
        return ServiceSignUpActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.service_login) {
            startActivity(new Intent(ServiceSignUpActivity.this, ServiceLoginActivity.class));

        } else if (v.getId() == R.id.tv_term_con_service) {
            Toast.makeText(this, "Term Condition yet To be described ", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_serv_sign_up) {
            if (TextUtils.isEmpty(Sname.getText().toString())) {
                Sname.setError("Enter your Name");
                return;
            } else if (TextUtils.isEmpty(Semail.getText().toString())) {
                Semail.setError("Enter Your email address");
                return;
            } else if (!MyApp.isEmailValid(Semail.getText().toString())) {
                Semail.setError("Enter a valid email address");
                return;
            } else if (TextUtils.isEmpty(Sphone.getText().toString())) {
                Sphone.setError("Enter mobile number");
                return;
            } else if (!servcheckBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
                return;
            }
            MyApp.setSharedPrefString("name", Sname.getText().toString());
            MyApp.setSharedPrefString("email", Semail.getText().toString());
            phVerification();

        }
    }


    private void phVerification() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);


        final String phone_no;
        phone_no = serv_countryCodePicker.getSelectedCountryCodeWithPlus() + " " + Sphone.getText().toString();
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
                Intent intent = new Intent(ServiceSignUpActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "service_signup");
                intent.putExtra("phone", phone_no);
                intent.putExtra("isRegister", true);
                intent.putExtra("isProvider", true);
                startActivity(intent);
            }
        });
        dialog.show();

    }


}
