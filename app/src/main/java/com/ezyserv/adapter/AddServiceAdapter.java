package com.ezyserv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.R.id.start;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class AddServiceAdapter extends RecyclerView.Adapter<AddServiceAdapter.DataHolder> {

    private List<DummyListItem> listdata;
    private LayoutInflater inflater;
    private ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }

    public void SetItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }


    public AddServiceAdapter(List<DummyListItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.add_service_item, parent, false);
        return new DataHolder(view);

    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        DummyListItem item = listdata.get(position);
        holder.Sname.setText(item.getService());
        holder.Sadd_remove.setText(item.getAction());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        TextView Sname, Sadd_remove;


        public DataHolder(final View itemView) {
            super(itemView);
            Sname = (TextView) itemView.findViewById(R.id.tv_serv_name);
            Sadd_remove = (TextView) itemView.findViewById(R.id.tv_add_remove);

            Sadd_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Sadd_remove.getText().equals("Add")) {
                        Sadd_remove.setText("Remove");
                        Sadd_remove.setTextColor(Color.parseColor("#3949AB"));
                        Sname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shape, 0, 0, 0);
                        count++;
                    } else if(Sadd_remove.getText().equals("Remove")) {
                        Sadd_remove.setText("Add");
                        Sadd_remove.setTextColor(Color.parseColor("#ED365B"));
                        Sname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        count--;
                    }

                }

            });

        }


    }

    public void setListData(ArrayList<DummyListItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }
}
