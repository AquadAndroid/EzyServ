package com.ezyserv.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezyserv.R;
import com.ezyserv.ServicemanProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJ-PC on 7/6/2017.
 */

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.DataHolder> {

    private List<DummyPaintingItem> listdata;
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


    public PaintingAdapter(List<DummyPaintingItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
        this.context = c;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.painting_item, parent, false);
        return new DataHolder(view);

    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        DummyPaintingItem item = listdata.get(position);

        holder.ServicemanImage.setImageResource(item.getServicemanImage());

        holder.ServicemanName.setText(item.getServicemanName());
        holder.ServiceAddress.setText(item.getServiceAddress());
        holder.ServicesProvided.setText(item.getServicesProvided());
        holder.ServiceRating.setText(item.getServiceRating());
        holder.AvailabilityMode.setText(item.getAvailabilityMode());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        TextView ServicemanName,ServiceAddress, ServicesProvided,ServiceRating,AvailabilityMode;
        ImageView ServicemanImage;
        LinearLayout painting_item_lnr;

        public DataHolder(final View itemView) {
            super(itemView);

            ServicemanImage=(ImageView)itemView.findViewById(R.id.serviceman_img);

            ServicemanName=(TextView)itemView.findViewById(R.id.serviceman_name);
            ServiceAddress=(TextView)itemView.findViewById(R.id.tv_address);
            ServicesProvided=(TextView)itemView.findViewById(R.id.tv_service_offered);
            ServiceRating=(TextView)itemView.findViewById(R.id.tv_avg_rating);
            AvailabilityMode=(TextView)itemView.findViewById(R.id.tv_availability_mode);
            painting_item_lnr=(LinearLayout)itemView.findViewById(R.id.paining_item_lnr);

            painting_item_lnr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(context, ServicemanProfileActivity.class));
                }
            });


        }


    }

    public void setListData(ArrayList<DummyPaintingItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }
}
