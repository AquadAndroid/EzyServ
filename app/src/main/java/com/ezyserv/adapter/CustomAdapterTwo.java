package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 3/16/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ezyserv.R;
import com.ezyserv.model.Services;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapterTwo extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<Services> servicelist;
    private Context context;

    public CustomAdapterTwo(Context context, List<Services> servicelist) {
        this.context = context;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servicelist = servicelist;
    }

    @Override
    public int getCount() {
        return servicelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.service_item, parent, false);

            listViewHolder.img_service = (CircleImageView) convertView.findViewById(R.id.img_service);
      
            listViewHolder.tv_service_name = (TextView) convertView.findViewById(R.id.tv_service_name);
        
          

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            Picasso.with(context).load(servicelist.get(position).getImage());
        } catch (Exception e) {
            listViewHolder.img_service.setImageResource(R.drawable.ac_installation);
        }
        /*String string = "\u20B9";
        byte[] utf8 = null;
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        listViewHolder.tv_service_name.setText(servicelist.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        CircleImageView img_service;
        TextView  tv_service_name;

    }

}
