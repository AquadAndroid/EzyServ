package com.ezyserv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezyserv.Painting;
import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.DataHolder> {

    private List<DummyServiceItem> listdata;
    private LayoutInflater inflater;
    private ItemClickCallback itemclickcallback;
    private int count = 0;
    private Context context;
    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }







    public void SetItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }


    public ServiceAdapter(List<DummyServiceItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
        this.context = c;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.domestic_service_item, parent, false);
        return new DataHolder(view);

    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        DummyServiceItem item = listdata.get(position);
        holder.ServiceName.setText(item.getServiceName());
        holder.ServiceIcon.setImageResource(item.getServiceIcon());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        RelativeLayout Domestic_item;
        TextView ServiceName;
        ImageView ServiceIcon;


        public DataHolder(final View itemView) {
            super(itemView);
            ServiceIcon=(ImageView)itemView.findViewById(R.id.service_icon);
            ServiceName=(TextView)itemView.findViewById(R.id.service_name);
            Domestic_item=(RelativeLayout)itemView.findViewById(R.id.rel_domestic_item);
            Domestic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(context, Painting.class));
                }
            });


        }


    }

    public void setListData(ArrayList<DummyServiceItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }
}
