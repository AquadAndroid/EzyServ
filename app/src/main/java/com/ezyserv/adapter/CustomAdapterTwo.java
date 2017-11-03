package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 3/16/2016.
 */

import android.content.Context;
import android.text.TextUtils;
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

import static android.R.attr.data;

public class CustomAdapterTwo extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<Services> servicelist;
  //  private Services data;

    private LayoutInflater inflater;
    public int count = 0;
    private Context c;
    //Services.Data item = data.getServices().get(position);
    private Context context;

    public CustomAdapterTwo(Context context, List<Services> servicelist) {
        this.context = context;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servicelist = servicelist;
    }

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }

  /*  public CustomAdapterTwo(Services data, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.data = data;
        this.c = c;
    }
*/
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

           /* Services.Data item = data.getServices().get(position);
    if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.service_item, parent, false);

            listViewHolder.img_service = (CircleImageView) convertView.findViewById(R.id.img_service);
            listViewHolder.tv_service_name = (TextView) convertView.findViewById(R.id.tv_service_name);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }

        holder.Sname.setText(item.getName());
        holder.Sadd_remove.setText(item.getAction());
        if (TextUtils.isEmpty(item.getAction())) {
            holder.Sadd_remove.setText("Add");
        }

        Picasso.with(c)
                .load(item.getImage())
//                .placeholder(R.drawable.ic_not_loaded) // optional
//                .error(R.drawable.ic_not_loaded)         // optional
                .into(holder.img_service);*/


        Services.Data item = servicelist.get(position).getServices().get(position);
        ViewHolder listViewHolder;
        listViewHolder = new ViewHolder();
        try {
            Picasso.with(context).load(item.getImage());
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


        listViewHolder.tv_service_name.setText(item.getName());
        return convertView;
    }

    static class ViewHolder {
        CircleImageView img_service;
        TextView  tv_service_name;

    }

}
