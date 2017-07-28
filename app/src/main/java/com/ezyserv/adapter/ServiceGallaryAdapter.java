package com.ezyserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/26/2017.
 */

public class ServiceGallaryAdapter extends RecyclerView.Adapter<ServiceGallaryAdapter.DataHolder> {
    private List<DummyGallaryItem> listdata;
    private LayoutInflater inflater;
    private ServiceGallaryAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final ServiceGallaryAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public ServiceGallaryAdapter(List<DummyGallaryItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public ServiceGallaryAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.service_gallery_item, parent, false);
        return new ServiceGallaryAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(ServiceGallaryAdapter.DataHolder holder, int position) {
        DummyGallaryItem item = listdata.get(position);
        holder.ServiceImage.setImageResource(item.getServiceImage());


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        ImageView ServiceImage;


        public DataHolder(final View itemView) {
            super(itemView);
            ServiceImage = (ImageView) itemView.findViewById(R.id.gallery_img);


        }


    }

    public void setListData(ArrayList<DummyGallaryItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }


}


