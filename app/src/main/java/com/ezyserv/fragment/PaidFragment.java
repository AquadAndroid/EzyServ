package com.ezyserv.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezyserv.R;
import com.ezyserv.adapter.DummyPaidData;
import com.ezyserv.adapter.DummySavedAddressData;
import com.ezyserv.adapter.PaidAdapter;
import com.ezyserv.adapter.SavedAddressAdapter;

import java.util.ArrayList;


public class PaidFragment extends Fragment {

    private RecyclerView paid_recyclerView;
    private PaidAdapter adapter;
    private ArrayList listdata;

    public PaidFragment() {
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
        View myView = inflater.inflate(R.layout.fragment_paid, container, false);
        paid_recyclerView = (RecyclerView) myView.findViewById(R.id.recy_paid_money);
        listdata = (ArrayList) DummyPaidData.getListData();

        paid_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PaidAdapter(listdata, getContext());
        paid_recyclerView.setAdapter(adapter);
        return myView;
    }


}
