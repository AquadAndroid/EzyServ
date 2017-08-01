package com.ezyserv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezyserv.R;

/**
 * Created by HP on 8/1/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    int SpinnerIcons[];
    String[] SpinnerText;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, int[] SpinnerIcons, String[] SpinnerText) {
        this.context = applicationContext;
        this.SpinnerIcons = SpinnerIcons;
        this.SpinnerText = SpinnerText;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return SpinnerIcons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.spiner_img);
        TextView names = (TextView) view.findViewById(R.id.tv_spinner);
        icon.setImageResource(SpinnerIcons[i]);
        names.setText(SpinnerText[i]);
        return view;
    }
}
