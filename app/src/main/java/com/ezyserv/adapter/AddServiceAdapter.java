package com.ezyserv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezyserv.R;
import com.ezyserv.model.Services;

import java.util.HashMap;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class AddServiceAdapter extends RecyclerView.Adapter<AddServiceAdapter.DataHolder> {

    private Services data;
    private LayoutInflater inflater;
    public int count = 0;
    private Context c;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);
    }

    public AddServiceAdapter(Services data, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.data = data;
        this.c = c;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.add_service_item, parent, false);
        return new DataHolder(view);

    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        Services.Data item = data.getServices().get(position);
        holder.Sname.setText(item.getName());
        holder.Sadd_remove.setText(item.getAction());
        if (TextUtils.isEmpty(item.getAction())) {
            holder.Sadd_remove.setText("Add");
        }

        Glide.with(c)
                .load(item.getImage())
//                .placeholder(R.drawable.ic_not_loaded) // optional
//                .error(R.drawable.ic_not_loaded)         // optional
                .into(holder.img_service);
    }

    @Override
    public int getItemCount() {
        return data.getServices().size();
    }

    public HashMap<String, String> idMap = new HashMap<>();

    class DataHolder extends RecyclerView.ViewHolder {
        TextView Sname, Sadd_remove;
        ImageView img_service;

        public DataHolder(final View itemView) {
            super(itemView);
            Sname = (TextView) itemView.findViewById(R.id.tv_serv_name);
            Sadd_remove = (TextView) itemView.findViewById(R.id.tv_add_remove);
            img_service = (ImageView) itemView.findViewById(R.id.img_service);

            Sadd_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Sadd_remove.getText().equals("Add")) {
                        Sadd_remove.setText("Remove");
                        Sadd_remove.setTextColor(Color.parseColor("#3949AB"));
                        idMap.put(data.getServices().get(getLayoutPosition()).getId(), "");
                        count++;
                    } else if (Sadd_remove.getText().equals("Remove")) {
                        Sadd_remove.setText("Add");
                        Sadd_remove.setTextColor(Color.parseColor("#ED365B"));
                        idMap.remove(data.getServices().get(getLayoutPosition()).getId());
                        count--;
                    }
                }

            });

        }
    }
}
