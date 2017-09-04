package com.ezyserv;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ezyserv.custome.CustomActivity;
import com.hbb20.CountryCodePicker;

public class ProfileUpdateActivity extends CustomActivity {
    private EditText edt_service_name, edt_service_detail_email, edt_service_mobile, edt_company, edt_doc_1, edt_doc_two, edt_doc;
    private TextView tv_doc1_change, tv_doc2_change, tv_upload;
    private Toolbar toolbar;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.profile_toolbar_title);
        mTitle.setText("Update Profile");
       final TextView mAction = (TextView) toolbar.findViewById(R.id.tv_edit_save);
        mAction.setText("EDIT");
        actionBar.setTitle("");
        setupUiElements();

        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAction.setText("SAVE");
                edt_service_name.setEnabled(true);
                edt_service_detail_email.setEnabled(true);
                edt_service_mobile.setEnabled(true);
                edt_company.setEnabled(true);
                edt_doc_1.setEnabled(true);
                edt_doc_two.setEnabled(true);
                edt_doc.setEnabled(true);

                ccp.setEnabled(true);

                tv_doc1_change.setVisibility(View.VISIBLE);
                tv_doc2_change.setVisibility(View.VISIBLE);
                tv_upload.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUiElements() {
        setClick(R.id.tv_edit_save);

        edt_service_name = (EditText) findViewById(R.id.edt_service_name);
        edt_service_detail_email = (EditText) findViewById(R.id.edt_service_detail_email);
        edt_service_mobile = (EditText) findViewById(R.id.edt_service_mobile);
        edt_company = (EditText) findViewById(R.id.edt_company);
        edt_doc_1 = (EditText) findViewById(R.id.edt_doc_1);
        edt_doc_two = (EditText) findViewById(R.id.edt_doc_two);
        edt_doc = (EditText) findViewById(R.id.edt_doc);

        edt_service_name.setEnabled(false);
        edt_service_detail_email.setEnabled(false);
        edt_service_mobile.setEnabled(false);
        edt_company.setEnabled(false);
        edt_doc_1.setEnabled(false);
        edt_doc_two.setEnabled(false);
        edt_doc.setEnabled(false);


        tv_doc1_change = (TextView) findViewById(R.id.tv_doc1_change);
        tv_doc2_change = (TextView) findViewById(R.id.tv_doc2_change);
        tv_upload = (TextView) findViewById(R.id.tv_upload);


        tv_doc1_change.setVisibility(View.GONE);
        tv_doc2_change.setVisibility(View.GONE);
        tv_upload.setVisibility(View.GONE);

        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        ccp.setEnabled(false);

    }



/*    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_edit_save) {
            mAction.set

        }
        }*/

}
