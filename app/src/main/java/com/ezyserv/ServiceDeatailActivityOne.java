package com.ezyserv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.utills.AppConstant;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class ServiceDeatailActivityOne extends CustomActivity implements CustomActivity.ResponseCallback {
    private Toolbar Dtoolbar;
    private Button btn_continue;
    private TextView verifyemail;
    private EditText demail;
    private EditText edt_service_name;
    private EditText edt_service_mobile;
    private String value = "";
    private CountryCodePicker ccp;
    private TextView txt_upload1, txt_upload2, txt_doc1, txt_doc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startActivity(new Intent(getContext(), MainActivity.class).putExtra("showAlert", true));
        setResponseListener(this);
        setContentView(R.layout.activity_service_deatail_one);
        Dtoolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_title);
        TextView mCount = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_count);
        mTitle.setText("Add your details");
        mCount.setText("1/2");
        actionBar.setTitle("");
        demail = (EditText) findViewById(R.id.edt_service_detail_email);
        edt_service_name = (EditText) findViewById(R.id.edt_service_name);
        edt_service_mobile = (EditText) findViewById(R.id.edt_service_mobile);
        verifyemail = (TextView) findViewById(R.id.verify_email);
        btn_continue = (Button) findViewById(R.id.btn_serv_detail_sign_up);
        txt_doc1 = (TextView) findViewById(R.id.txt_doc1);
        txt_doc2 = (TextView) findViewById(R.id.txt_doc2);
        txt_upload1 = (TextView) findViewById(R.id.txt_upload1);
        txt_upload2 = (TextView) findViewById(R.id.txt_upload2);
        setTouchNClick(R.id.txt_upload1);
        setTouchNClick(R.id.txt_upload2);
        demail.setText(MyApp.getSharedPrefString("email"));
        edt_service_name.setText(MyApp.getSharedPrefString("name"));
        edt_service_mobile.setText(MyApp.getSharedPrefString("phone"));

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setCcpClickable(false);
     //   ccp.setCountryForPhoneCode(Integer.parseInt(edt_service_mobile.getText().toString().split(" ")[0].replace("+", "")));

        value = getIntent().getStringExtra("key");
        value = "email_verification";

        if (value.equals("email_verification")) {
            verifyemail.setVisibility(View.GONE);
            demail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shape, 0);
        } else if (value.equals("phone_verified")) {
            verifyemail.setVisibility(View.VISIBLE);
            demail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            verifyemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ServiceDeatailActivityOne.this, ServiceEmailVerificationActivity.class));
                }
            });
        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleInstance.getInstance().setServicesId("");
                startActivity(new Intent(ServiceDeatailActivityOne.this, ServiceDetailActivityTwo.class));
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == txt_upload2) {
            showFileChooser(FILE_2);
        } else if (v == txt_upload1) {
            showFileChooser(FILE_1);
        }
    }

    private static final int FILE_1 = 121;
    private static final int FILE_2 = 151;

    private void showFileChooser(int fileNumber) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    fileNumber);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("onActivityResult", "File Uri: " + uri.toString());
                    String path = null;
                    try {
                        path = MyApp.getPath(getContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("onActivityResult", "File Path: " + path);
                    String[] bits = path.split("/");
                    String lastOne = bits[bits.length - 1];
                    txt_doc1.setText(lastOne);
                    txt_upload1.setText("Change");
                    // Get the file instance
                    File file = new File(path);
                    uploadDoc(file, FILE_1);
                    // Initiate the upload
                }
                break;

            case FILE_2:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("onActivityResult", "File Uri: " + uri.toString());
                    String path = null;
                    try {
                        path = MyApp.getPath(getContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("onActivityResult", "File Path: " + path);
                    String[] bits = path.split("/");
                    String lastOne = bits[bits.length - 1];
                    txt_doc2.setText(lastOne);
                    txt_upload2.setText("Change");
                    File file = new File(path);
                    uploadDoc(file, FILE_2);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadDoc(File f, int number) {
        RequestParams p = new RequestParams();
        p.put("user_id", MyApp.getApplication().readUser().getId());
        String contentType = RequestParams.APPLICATION_OCTET_STREAM;
        try {
            if (number == FILE_1)
                p.put("docs1", f, contentType);
            else
                p.put("docs2", f, contentType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        postCall(getContext(), AppConstant.BASE_URL + "updateDocs", p, "Uploading...", 1);
    }

    private Context getContext() {
        return ServiceDeatailActivityOne.this;
    }

    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optString("status").equals("true")) {
                MyApp.showMassage(getContext(), "Document Uploaded successfully.");
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
//        MyApp.popMessage("Error", error, getContext());
        MyApp.showMassage(getContext(), "Uploaded successfully");
    }
}
