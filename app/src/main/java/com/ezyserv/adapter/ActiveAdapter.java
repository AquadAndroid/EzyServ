package com.ezyserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/26/2017.
 */

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.DataHolder> {
    private List<DummyAddedItem> listdata;
    private LayoutInflater inflater;
    private ActiveAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final ActiveAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public ActiveAdapter(List<DummyAddedItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public ActiveAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.addded_money_item, parent, false);
        return new ActiveAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(ActiveAdapter.DataHolder holder, int position) {
        DummyAddedItem item = listdata.get(position);
        holder.AdddedLabel.setText(item.getAddedLabel());
        holder.BankName.setText(item.getBankName());
        holder.Via.setText(item.getVia());
        holder.AdddedAmount.setText(item.getAddedAmount());
        holder.AddedDate.setText(item.getAddedDate());

    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }



    class DataHolder extends RecyclerView.ViewHolder {
        TextView AdddedLabel, BankName, Via, AdddedAmount, AddedDate;



        public DataHolder(final View itemView) {
            super(itemView);
            AdddedLabel = (TextView) itemView.findViewById(R.id.label_added_wallet);
            BankName = (TextView) itemView.findViewById(R.id.tv_bank_name);
            Via = (TextView) itemView.findViewById(R.id.tv_maode);
            AdddedAmount = (TextView) itemView.findViewById(R.id.tv_added_amt);
            AddedDate = (TextView) itemView.findViewById(R.id.tv_added_date);





        }


    }

    public void setListData(ArrayList<DummyAddedItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }




}


