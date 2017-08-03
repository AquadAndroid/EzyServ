package com.ezyserv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ezyserv.MainActivity;
import com.ezyserv.R;

import java.util.List;

/**
 * Created by Aquad on 03-08-2017.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<String> data;
    private Context context;

    public SwipeDeckAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater =  LayoutInflater.from(context);
            // normally use a viewholder
            v = inflater.inflate(R.layout.test_card, parent, false);
        }
//        ((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).serviceMenDetails();
//                String item = (String)getItem(position);
//                Log.i("MainActivity", item);
            }
        });

        return v;
    }
}