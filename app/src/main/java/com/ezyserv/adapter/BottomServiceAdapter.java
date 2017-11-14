package com.ezyserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezyserv.R;
import com.ezyserv.model.Services;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class BottomServiceAdapter extends RecyclerView.Adapter<BottomServiceAdapter.DataHolder> {

    //  private Services data;
    private List<Services.Data> servicelist;
    private LayoutInflater inflater;
    // public int count = 0;
    private Context c;
//    int catg;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }

    public BottomServiceAdapter(List<Services.Data> servicelist, Context c, int position) {
        this.inflater = LayoutInflater.from(c);
        this.servicelist = servicelist;
        this.c = c;
//        this.catg=position;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.service_item, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        Services.Data item = servicelist.get(position);
        holder.tv_service_name.setText(item.getName());
        Glide.with(c)
                .load(item.getImage())
                .into(holder.img_service);
    }

    @Override
    public int getItemCount() {
        return servicelist.size();
    }

    public HashMap<String, String> idMap = new HashMap<>();

    class DataHolder extends RecyclerView.ViewHolder {
        TextView tv_service_name;
        CircleImageView img_service;

        public DataHolder(final View itemView) {
            super(itemView);
            tv_service_name = (TextView) itemView.findViewById(R.id.tv_service_name);
            img_service = (CircleImageView) itemView.findViewById(R.id.img_service);

        }
    }
}
