package com.ezyserv.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ezyserv.MainActivity;
import com.ezyserv.R;
import com.ezyserv.ServiceDetailActivityTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/12/2017.
 */

public class ServiceLocationAdapter extends RecyclerView.Adapter<ServiceLocationAdapter.DataHolder> {


    private List<DummyLocList> listdata;
    private LayoutInflater inflater;
    private AddServiceAdapter.ItemClickCallback itemclickcallback;
    private Context context;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public ServiceLocationAdapter(List<DummyLocList> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
        this.context = c;
    }


    @Override
    public ServiceLocationAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.loacation_list_item, parent, false);
        return new DataHolder(view);

    }


    @Override
    public void onBindViewHolder(ServiceLocationAdapter.DataHolder holder, int position) {
        DummyLocList item = listdata.get(position);
        holder.Lname.setText(item.getLocation());
        holder.Llabel.setText(item.getLocLabel());
        holder.Ladd_remove.setText(item.getAction());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        TextView Lname, Ladd_remove, Llabel;


        public DataHolder(final View itemView) {
            super(itemView);
            Lname = (TextView) itemView.findViewById(R.id.service_location);
            Ladd_remove = (TextView) itemView.findViewById(R.id.tv_remove_add_location);
            Llabel = (TextView) itemView.findViewById(R.id.location_no_label);
            Ladd_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(context, MainActivity.class));

                  /*  try {
                        ((ServiceDetailActivityTwo) v.getContext()).dialogLocation();
                    } catch (Exception e) {
                        // ignore
                    }*/


                }

            });

        }


    }


    public void setListData(ArrayList<DummyLocList> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }


}
