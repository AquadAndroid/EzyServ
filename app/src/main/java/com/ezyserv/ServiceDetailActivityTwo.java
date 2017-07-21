package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ezyserv.adapter.AddServiceAdapter;
import com.ezyserv.adapter.DummyData;
import com.ezyserv.adapter.DummyLocation;
import com.ezyserv.adapter.LocationObject;
import com.ezyserv.adapter.ServiceLocationAdapter;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class ServiceDetailActivityTwo extends CustomActivity {
    private Toolbar Dtoolbar;
    private TextView tvDomestic, tvConstruction, tvEvents, tvAddLocation;
    private TextView countDomestic, countConstruction, countEvents;
    private RecyclerView listLocations;
    private Button Continue;
    private ArrayList listdata;
    private ServiceLocationAdapter adapter;


    ArrayList<LocationObject> dummyListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail_two);
        Dtoolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(Dtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) Dtoolbar.findViewById(R.id.detail_toolbar_title);
        TextView mCount=(TextView)Dtoolbar.findViewById(R.id.detail_toolbar_count);
        mTitle.setText("Add your details");
        mCount.setText("2/2");
        actionBar.setTitle("");
        setupuiElement();
    }

    private void setupuiElement() {

        setTouchNClick(R.id.tv_domestic);
        setTouchNClick(R.id.tv_construction);
        setTouchNClick(R.id.tv_events);
        setTouchNClick(R.id.btn_continue);


        tvDomestic=(TextView)findViewById(R.id.tv_domestic);
        tvConstruction=(TextView)findViewById(R.id.tv_construction);
        tvEvents=(TextView)findViewById(R.id.tv_events);
        tvAddLocation=(TextView)findViewById(R.id.edt_add_loaction);


        countDomestic=(TextView)findViewById(R.id.tv_domestic_label);
        countConstruction=(TextView)findViewById(R.id.tv_construction_label);
        countEvents=(TextView)findViewById(R.id.tv_events_label);
        listdata = (ArrayList) DummyLocation.getListData();
        listLocations=(RecyclerView)findViewById(R.id.location_list);

        listLocations.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ServiceLocationAdapter(listdata, this);
        listLocations.setAdapter(adapter);

      /*  dummyListItems = new ArrayList<>();
        dummyListItems.add(new LocationObject("Area & Location 1"," India ","ADD LOCATION"));
        dummyListItems.add(new LocationObject("Area & Location 2"," China ","ADD LOCATION"));
        dummyListItems.add(new LocationObject("Area & Location 3"," USA ","ADD LOCATION"));
        dummyListItems.add(new LocationObject("Area & Location 4"," Bhutan","ADD LOCATION"));
        dummyListItems.add(new LocationObject("Area & Location 5"," Malsiya","ADD LOCATION"));*/





        Continue=(Button)findViewById(R.id.btn_continue);



    }

    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.tv_domestic){
            Intent intent= new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key","domestic");
            startActivity(intent);
        }else if(v.getId()== R.id.tv_construction){
            Intent intent= new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key","construction");
            startActivity(intent);
        }else if(v.getId()== R.id.tv_events){
            Intent intent= new Intent(ServiceDetailActivityTwo.this, AddServicesActivity.class);
            intent.putExtra("key","events");
            startActivity(intent);
        }else if(v.getId()== R.id.btn_continue){
            //Toast.makeText(this, "The Project is under Process will be updated soon", Toast.LENGTH_LONG).show();
dialogLocation();
        }

    }




    public void dialogLocation(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.location_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       /* dialog.setTitle("Verification");
        dialog.setTitle( Html.fromHtml("<font color='#3949AB'>Verification</font>"));*/

       // String phone_no;
        //phone_no=countryCodePicker.getSelectedCountryCodeWithPlus()+" "+ cust_mobile_no.getText().toString();
       // TextView dialog_mb_no=(TextView)dialog.findViewById(R.id.tv_mb_no);
        //dialog_mb_no.setText(phone_no);
        //Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.ph_verify_cancel);
        //Button dialog_send_Button = (Button) dialog.findViewById(R.id.btn_send);
        // if button is clicked, close the custom dialog
     /*   dialog_cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/


      /*  dialog_send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CustomerLoginActivity.this, PhoneVerificationActivity.class);
                intent.putExtra("key", "customer_login");
                startActivity(intent);
            }
        });*/
        dialog.show();

    }
}
