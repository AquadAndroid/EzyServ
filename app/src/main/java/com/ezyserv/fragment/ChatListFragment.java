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
import com.ezyserv.adapter.ChatListAdapter;
import com.ezyserv.adapter.DummyChatData;

import java.util.ArrayList;


public class ChatListFragment extends Fragment {

    private RecyclerView recy_chat_list;


    private ChatListAdapter adapter;
    private ArrayList listdata;

    public ChatListFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recy_chat_list=(RecyclerView)view.findViewById(R.id.recy_chat_list);
        listdata = (ArrayList) DummyChatData.getListData();
        recy_chat_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ChatListAdapter(listdata, getActivity());
        recy_chat_list.setAdapter(adapter);
        return view;
    }
}
