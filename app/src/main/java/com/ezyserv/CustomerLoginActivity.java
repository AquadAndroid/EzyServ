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
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.RequestParams;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ezyserv.application.MyApp.initializeFramworkWithApp;

public class CustomerLoginActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    String TAG = "CustomerLoginActivity";
    private Toolbar toolbar;
    private EditText cust_mobile_no;
    private Button btn_login;
    private CheckBox checkBox;
    private CountryCodePicker countryCodePicker;
    private TextView cust_term_cond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setResponseListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Log In as Customer");
        actionBar.setTitle("");
        setupUiElement();

        //Initializing the App with QB
        initializeFramworkWithApp(this);

    }

    private void setupUiElement() {

        setTouchNClick(R.id.btn_login);
        setTouchNClick(R.id.custom_term_cond);

        cust_mobile_no = (EditText) findViewById(R.id.custom_mb_no);
        btn_login = (Button) findViewById(R.id.btn_login);
        checkBox = (CheckBox) findViewById(R.id.terms_condition_checkbox);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        cust_term_cond = (TextView) findViewById(R.id.custom_term_cond);
        String htmlString = "<u>Terms and condition</u>";
        cust_term_cond.setText(Html.fromHtml(htmlString));
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v == btn_login) {
            if (TextUtils.isEmpty(cust_mobile_no.getText().toString())) {
                cust_mobile_no.setError("Enter Mobile No");
                return;
            }/* else if (!checkBox.isChecked()) {
                Toast.makeText(this, "Please accept the terms and Condition", Toast.LENGTH_SHORT).show();
                return;
            }*/

            callLogin();

        } else if (v.getId() == R.id.custom_term_cond) {
            //Toast.makeText(this, "Terms and Condition", Toast.LENGTH_SHORT).show();
            MyApp.setStatus(AppConstant.IS_LOGIN, true);
//            MyApp.setSharedPrefString(AppConstant.LOGIN_TYPE,"Normal");
            startActivity(new Intent(CustomerLoginActivity.this, MainActivity.class));
            finishAffinity();
        }
    }

    private void callLogin() {
        RequestParams p = new RequestParams();
        p.put("phone", cust_mobile_no.getText().toString());
        postCall(getContext(), AppConstant.BASE_URL + "login", p, "Logging you in...", 1);
    }


    private void phVerification() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verification_dialog);

        final String phone_no;
        phone_no = countryCodePicker.getSelectedCountryCodeWithPlus() + " " + cust_mobile_no.getText().toString();
        TextView verification_message = (TextView) dialog.findViewById(R.id.verification_message);
        verification_message.setText("A Verification code will be sent to " + phone_no + " for verification.");
        Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.ph_verify_cancel);
        Button dialog_send_Button = (Button) dialog.findViewById(R.id.btn_send);
        dialog_cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog_send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CustomerLoginActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "customer_login");
                intent.putExtra("phone", phone_no);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (o.optString("status").equals("true")) {
            try {
                User u = new Gson().fromJson(o.getJSONObject("data").toString(), User.class);
                MyApp.getApplication().writeUser(u);
                if (u.getId().length() > 0) {
                    SignInUserForQBChat(u.getEmail());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            MyApp.popMessage("Error", o.optString("Message"), getContext());
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        MyApp.popMessage("Error", error, getContext());
    }

    private Context getContext() {
        return CustomerLoginActivity.this;
    }


    //LoginUser For Chat the user is trying to login
    void SignInUserForQBChat(String fullName) {
        Log.e(TAG, "SignInUserForQBChat: " + fullName);
        QBUser qbUser = new QBUser(fullName, "12345678");

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e(TAG, "onSuccess Login : " + qbUser.getId());
                phVerification();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: login " + e.toString());
            }
        });
    }
}
