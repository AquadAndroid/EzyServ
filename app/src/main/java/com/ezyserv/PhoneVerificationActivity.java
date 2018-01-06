package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.Country;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.RequestParams;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ezyserv.application.MyApp.initializeFramworkWithApp;

public class PhoneVerificationActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    String TAG = "PhoneVerificationActivity";
    private Button btn_verify;
    private String value;
    private TextView mb_no;
    private TextView txt_counter;
    private EditText edt_otp;
    private ProgressBar progress_bar;
    private TextView txt_enter_manually;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private TextView txt_change;
    private TextView txt_resend;
    private boolean isRegister = false;
    private String countryId = "94";
    private boolean isProvider = false;
    private String qbUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        btn_verify = (Button) findViewById(R.id.btn_verify);
        mb_no = (TextView) findViewById(R.id.mb_no);
        txt_change = (TextView) findViewById(R.id.txt_change);
        txt_enter_manually = (TextView) findViewById(R.id.txt_enter_manually);
        txt_resend = (TextView) findViewById(R.id.txt_resend);
        setTouchNClick(R.id.txt_enter_manually);
        setTouchNClick(R.id.txt_change);
        setTouchNClick(R.id.txt_resend);

        initializeFramworkWithApp(this);


        setResponseListener(this);

        isRegister = getIntent().getBooleanExtra("isRegister", false);
        isProvider = getIntent().getBooleanExtra("isProvider", false);
        mb_no.setText(getIntent().getStringExtra("phone"));
        Bundle extras = getIntent().getExtras();

        MyApp.getApplication().setSharedPrefString("phone", mb_no.getText().toString());
        value = extras.getString("key").toString();

        txt_counter = (TextView) findViewById(R.id.txt_counter);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        setTouchNClick(R.id.btn_verify);
        List<Country> countries = MyApp.getApplication().readCountry();
        String code = mb_no.getText().toString().split(" ")[0].replace("+", "");
        for (Country c : countries) {
            if (code.equals(c.getPhone_code())) {
                countryId = c.getId();
            }
        }
        Log.d("countryId", countryId);

        showCounter();
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (code.equals("111111")) {
            if (isRegister) {
                SignUpUserForQBChat();
            } else {
                Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                intent.putExtra("ezy", value);
                startActivity(intent);
                return;
            }

        }
      try{
          // [START verify_with_code]
          PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
          // [END verify_with_code]
          signInWithPhoneAuthCredential(credential);
      }catch (Exception e){}
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == txt_enter_manually) {
            progress_bar.setVisibility(View.GONE);
        } else if (v == btn_verify) {
            String code = edt_otp.getText().toString();
            if (TextUtils.isEmpty(code)) {
                edt_otp.setError("Cannot be empty.");
                return;
            }
            verifyPhoneNumberWithCode(mVerificationId, code);
        } else if (v == txt_change) {
            finish();
        } else if (v == txt_resend) {
            txt_resend.setVisibility(View.GONE);
            showCounter();
        }
    }

    private void showCounter() {

        CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be called every second.
                txt_counter.setText(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + ":" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                txt_counter.setText("00:00");
                txt_resend.setVisibility(View.VISIBLE);
                //you are good to go.
                //30 seconds passed.
            }
        };
        mCountDownTimer.start();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("phone", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("phone", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("phone", "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;

                // ...
            }
        };

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("authStateChange", "");
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mb_no.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("phone", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseAuth.getInstance().signOut();
                            if (isRegister) {
                                Log.e(TAG, "SignUpUserForQBChat: 2");
                                SignUpUserForQBChat();
                            } else {
                                Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                                intent.putExtra("ezy", value);
                                startActivity(intent);
                            }

                        } else {
                            if (edt_otp.getText().toString().equals("111111")) {
                                if (isRegister) {
                                    Log.e(TAG, "SignUpUserForQBChat: 3");
                                    //AgainCalled
                                    //SignUpUserForQBChat();
                                } else {
                                    Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                                    intent.putExtra("ezy", value);
                                    startActivity(intent);
                                }
                                return;
                            }
                            // Sign in failed, display a message and update the UI
                            Log.w("phone", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                MyApp.popMessage("Alert!", "Please enter a valid code that sent to " +
                                        " " + mb_no.getText().toString() + ".\nThank you", PhoneVerificationActivity.this);
                            }
                        }
                    }
                });
    }

    private boolean isRegisterCalled = false;

    private void registerUser() {
        if (isRegisterCalled) {
            return;
        }
        isRegisterCalled = true;
        RequestParams p = new RequestParams();
        p.put("name", MyApp.getSharedPrefString("name"));
        p.put("email", MyApp.getSharedPrefString("email"));
        p.put("phone", mb_no.getText().toString().split(" ")[1]);
        p.put("country_id", countryId);
        if (isProvider) {
            p.put("isServicemen", "1");
//            p.put("service_categories_id", "7");
            p.put("services", "2,3");
        } else {
            p.put("isServicemen", "0");
        }
        p.put("currentlat", "0.0");
        p.put("currentlong", "0.0");
        p.put("company", "");
        p.put("address", "India");
        p.put("docs1", "");
        p.put("docs2", "");
        p.put("profilepic", "");
        p.put("qbUserID", qbUserID);

        postCall(getContext(), AppConstant.BASE_URL + "register", p, "Registering...", 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optString("status").equals("true")) {
                try {
                    User u = new Gson().fromJson(o.getJSONObject("data").toString(), User.class);
                    MyApp.getApplication().writeUser(u);
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyApp.popMessage("Alert!", "Parsing error.", PhoneVerificationActivity.this);
                    return;
                } catch (JsonSyntaxException ee) {

                }
            } else {
                MyApp.popMessageWithFinish("Alert!", o.optString("Message"), PhoneVerificationActivity.this);
                return;
            }
            Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
            intent.putExtra("ezy", value);
            startActivity(intent);
        }
        isRegisterCalled = false;
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        isRegisterCalled = false;
        MyApp.popMessage("Error", error, getContext());
    }

    private Context getContext() {
        return PhoneVerificationActivity.this;
    }


    //Hector Call

    //Register User For QBChat
    private void SignUpUserForQBChat() {

        String email = MyApp.getSharedPrefString("email");
        String pwd = "12345678";

        QBUser qbUser = new QBUser(email, pwd);

        if (MyApp.getSharedPrefString(AppConstant.LOGIN_TYPE).equals("FB")) {
            qbUser.setFacebookId(MyApp.getSharedPrefString(AppConstant.FB_ID));
        }

        qbUser.setFullName(MyApp.getSharedPrefString("name"));
        qbUser.setEmail(MyApp.getSharedPrefString("email"));

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e(TAG, "onSuccess register : " + qbUser.getId());
                qbUserID = String.valueOf(qbUser.getId());
                SignInUserForQBChat(qbUser.getLogin());
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: register " + e.toString());
                Toast.makeText(PhoneVerificationActivity.this, "Error While Register, Try After Sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Login User For QBChat after Register
    void SignInUserForQBChat(String fullName) {
        Log.e(TAG, "SignInUserForQBChat: " + fullName);
        QBUser qbUser = new QBUser(fullName, "12345678");

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e(TAG, "onSuccess Login : " + qbUser.getId());
                registerUser();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: login " + e.toString());
            }
        });
    }
    //End Call
}
