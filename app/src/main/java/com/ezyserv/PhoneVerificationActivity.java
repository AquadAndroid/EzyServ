package com.ezyserv;

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

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
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

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends CustomActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        btn_verify = (Button) findViewById(R.id.btn_verify);
        mb_no = (TextView) findViewById(R.id.mb_no);
        txt_change = (TextView) findViewById(R.id.txt_change);
        txt_enter_manually = (TextView) findViewById(R.id.txt_enter_manually);
        setTouchNClick(R.id.txt_enter_manually);
        setTouchNClick(R.id.txt_change);

        mb_no.setText(getIntent().getStringExtra("phone"));
        Bundle extras = getIntent().getExtras();
//
        MyApp.getApplication().setSharedPrefString("phone", mb_no.getText().toString());
        value = extras.getString("key").toString();

        txt_counter = (TextView) findViewById(R.id.txt_counter);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        showCounter();
        setTouchNClick(R.id.btn_verify);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
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
                            Intent intent = new Intent(PhoneVerificationActivity.this, SucessfullLoginActivity.class);
                            intent.putExtra("ezy", value);
                            startActivity(intent);
                            // ...
                        } else {
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
}
