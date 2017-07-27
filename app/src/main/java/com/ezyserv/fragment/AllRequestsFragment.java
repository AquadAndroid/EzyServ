package com.ezyserv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezyserv.R;
import com.ezyserv.adapter.AddedAdapter;
import com.ezyserv.adapter.DummyAddedData;

import java.util.ArrayList;


public class AllRequestsFragment extends Fragment {

    private RecyclerView added_recyclerView;
    private AddedAdapter adapter;
    private ArrayList listdata;
    public AllRequestsFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_all_requests, container, false);
        added_recyclerView = (RecyclerView) myView.findViewById(R.id.recy_added_money);
        listdata = (ArrayList) DummyAddedData.getListData();

        added_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AddedAdapter(listdata, getContext());
        added_recyclerView.setAdapter(adapter);
        return myView;
    }






}
