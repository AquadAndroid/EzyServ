package com.ezyserv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezyserv.AddServicesActivity;
import com.ezyserv.R;
import com.ezyserv.ServiceDetailActivityTwo;
import com.ezyserv.application.MyApp;
import com.ezyserv.application.SingleInstance;
import com.ezyserv.model.Services;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class AddServiceAdapter extends RecyclerView.Adapter<AddServiceAdapter.DataHolder> {
    String TAG = AddServiceAdapter.class.getSimpleName();
    private Services data;
    private LayoutInflater inflater;
    public int count = 0;
    private Context c;
    private boolean isPrimary;
    private boolean isCompany;
    private int selected = 0;

    public HashMap<String, String> idMap = new HashMap<>();

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }

    public AddServiceAdapter(Services data, Context c, boolean isPrimary, boolean isCompany) {
        this.inflater = LayoutInflater.from(c);
        this.data = data;
        this.c = c;
        this.isPrimary = isPrimary;
        this.isCompany = isCompany;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.add_service_item, parent, false);
        return new DataHolder(view);

    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int position) {
        Services.Data item = data.getServices().get(position);
        holder.Sname.setText(item.getName());
        holder.Sadd_remove.setText(item.getAction());
        if (TextUtils.isEmpty(item.getAction())) {
            holder.Sadd_remove.setText("Add");
        }

        Picasso.with(c)
                .load(item.getImage())
                .into(holder.img_service);

        holder.Sadd_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPrimary) {
                    if (holder.Sadd_remove.getText().equals("Add")) {
                        ((AddServicesActivity) c).setPrimaryName(data.getServices().get(position).getName(),
                                data.getServices().get(position).getId());
                    } else {
                        ((AddServicesActivity) c).setPrimaryName("Primary Service", "Primary Service");
                    }
                    return;
                }
                if (!isCompany && selected >= 5 && holder.Sadd_remove.getText().equals("Add")) {
                    MyApp.popMessage("Alert!", "You can add only 5 services as individual", c);
                    return;
                }
                if (holder.Sadd_remove.getText().equals("Add")) {

                    if (!SingleInstance.getInstance().getPrimaryName().split("@")[1].equals("null")) {
                        if (data.getServices().get(position).getId().equals(SingleInstance.getInstance().getPrimaryName().split("@")[1])) {
                            Toast.makeText(c, "You had already selected this service as primary.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (SingleInstance.getInstance().getServicesId().contains(data.getServices().get(position).getId())) {
                                Toast.makeText(c, "You had already selected this service.", Toast.LENGTH_SHORT).show();
                            } else {
                                ++selected;
                                holder.Sadd_remove.setText("Remove");
                                holder.Sadd_remove.setTextColor(Color.parseColor("#3949AB"));
                                idMap.put(data.getServices().get(position).getId(), "");
                                count++;
                            }
                        }
                    } else {
                        ++selected;
                        holder.Sadd_remove.setText("Remove");
                        holder.Sadd_remove.setTextColor(Color.parseColor("#3949AB"));
                        idMap.put(data.getServices().get(position).getId(), "");
                        count++;
                    }

                } else if (holder.Sadd_remove.getText().equals("Remove")) {
                    --selected;
                    holder.Sadd_remove.setText("Add");
                    holder.Sadd_remove.setTextColor(Color.parseColor("#ED365B"));
                    idMap.remove(data.getServices().get(position).getId());
                    count--;
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return data.getServices().size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        TextView Sname, Sadd_remove;
        ImageView img_service;

        public DataHolder(final View itemView) {
            super(itemView);
            Sname = itemView.findViewById(R.id.tv_serv_name);
            Sadd_remove = itemView.findViewById(R.id.tv_add_remove);
            img_service = itemView.findViewById(R.id.img_service);
        }
    }


}
