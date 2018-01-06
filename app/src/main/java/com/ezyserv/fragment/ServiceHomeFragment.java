package com.ezyserv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezyserv.AddMoneyServicesActivity;
import com.ezyserv.ProfileUpdateActivity;
import com.ezyserv.R;
import com.ezyserv.ServiceDetailActivityTwo;
import com.ezyserv.adapter.SpinnerAdapterTwo;


public class ServiceHomeFragment extends Fragment implements View.OnClickListener {
    String[] SpinnerText = {"Available", "Busy", "Offline"};
    int SpinnerIcons[] = {R.drawable.green_dot, R.drawable.red_dot, R.drawable.gray_dot};
    private Spinner avialbility_spiner;

    private TextView tv_profile_incomplete, tv_low_balance;
    private TextView tv_edit_profile, tv_view_add_services, tv_add_location, tv_recharge_profile;
    private TextView tv_business_details, tv_service_count, tv_location_count, tv_wallet_bal;

    public ServiceHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_service_home, container, false);
        avialbility_spiner = (Spinner) myView.findViewById(R.id.avialbility_spiner);
        avialbility_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SpinnerAdapterTwo customAdapter = new SpinnerAdapterTwo(getContext(), SpinnerIcons, SpinnerText);
        avialbility_spiner.setAdapter(customAdapter);


        tv_profile_incomplete = (TextView) myView.findViewById(R.id.tv_profile_incomplete);
        tv_low_balance = (TextView) myView.findViewById(R.id.tv_low_balance);


        tv_edit_profile = (TextView) myView.findViewById(R.id.tv_edit_profile);
        tv_view_add_services = (TextView) myView.findViewById(R.id.tv_view_add_services);
        tv_add_location = (TextView) myView.findViewById(R.id.tv_add_location);
        tv_recharge_profile = (TextView) myView.findViewById(R.id.tv_recharge_profile);


        tv_business_details = (TextView) myView.findViewById(R.id.tv_business_details);
        tv_service_count = (TextView) myView.findViewById(R.id.tv_service_count);
        tv_location_count = (TextView) myView.findViewById(R.id.tv_location_count);
        tv_wallet_bal = (TextView) myView.findViewById(R.id.tv_wallet_bal);




        tv_edit_profile.setOnClickListener(this);
        tv_view_add_services.setOnClickListener(this);
        tv_add_location.setOnClickListener(this);
        tv_recharge_profile.setOnClickListener(this);

        return myView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_profile:
            {
                startActivity(new Intent(getActivity(), ProfileUpdateActivity.class));
                break;
            }
            case R.id.tv_view_add_services:
            {
                startActivity(new Intent(getActivity(), ServiceDetailActivityTwo.class));
                break;
            }
            case R.id.tv_add_location:
            {
                startActivity(new Intent(getActivity(), ProfileUpdateActivity.class));
                break;
            }
            case R.id.tv_recharge_profile:
            {
                startActivity(new Intent(getActivity(), AddMoneyServicesActivity.class));
                break;
            }
        }
    }
}
