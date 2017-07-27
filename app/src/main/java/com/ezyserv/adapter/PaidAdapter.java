package com.ezyserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 7/26/2017.
 */

public class PaidAdapter extends RecyclerView.Adapter<PaidAdapter.DataHolder> {
    private List<DummyPaidItem> listdata;
    private LayoutInflater inflater;
    private PaidAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final PaidAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public PaidAdapter(List<DummyPaidItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public PaidAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.paid_money_item, parent, false);
        return new PaidAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(PaidAdapter.DataHolder holder, int position) {
        DummyPaidItem item = listdata.get(position);
        holder.ServiceName.setText(item.getServiceName());
        holder.ServiceManName.setText(item.getServiceMan());
        holder.OrderId.setText(item.getOrderId());
        holder.PaidAmount.setText(item.getPaidAmount());
        holder.PaidDate.setText(item.getPaidDate());

    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }



    class DataHolder extends RecyclerView.ViewHolder {
        TextView ServiceName, ServiceManName, OrderId, PaidAmount, PaidDate;
        CircleImageView ServiceLogo;


        public DataHolder(final View itemView) {
            super(itemView);
            ServiceName = (TextView) itemView.findViewById(R.id.tv_service_name);
            ServiceManName = (TextView) itemView.findViewById(R.id.tv_serviceman_name);
            OrderId = (TextView) itemView.findViewById(R.id.tv_order_id);
            PaidAmount = (TextView) itemView.findViewById(R.id.tv_paid_money);
            PaidDate = (TextView) itemView.findViewById(R.id.tv_paid_date);
            ServiceLogo=(CircleImageView)itemView.findViewById(R.id.img_profile);




        }


    }

    public void setListData(ArrayList<DummyPaidItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }




}


