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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 7/26/2017.
 */

public class CustomerReviewAdapter extends RecyclerView.Adapter<CustomerReviewAdapter.DataHolder> {
    private List<DummyReviewItem> listdata;
    private LayoutInflater inflater;
    private CustomerReviewAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final CustomerReviewAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public CustomerReviewAdapter(List<DummyReviewItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public CustomerReviewAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.customer_review_item, parent, false);
        return new CustomerReviewAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(CustomerReviewAdapter.DataHolder holder, int position) {
        DummyReviewItem item = listdata.get(position);

        holder.CustomerName.setText(item.getCustomerName());
        holder.CustomerRating.setText(item.getCustomerRating());
        holder.CustomerReview.setText(item.getCustomerReview());
        holder.ProfileImage.setImageResource(item.getProfileImage());
        holder.ServiceImage.setImageResource(item.getServiceImage());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        ImageView ServiceImage;
        CircleImageView ProfileImage;
        TextView CustomerName,CustomerRating,CustomerReview;


        public DataHolder(final View itemView) {
            super(itemView);

            CustomerName = (TextView) itemView.findViewById(R.id.tv_custm_name);
            CustomerRating = (TextView) itemView.findViewById(R.id.tv_cust_rating);
            CustomerReview = (TextView) itemView.findViewById(R.id.tv_cust_review);

            ProfileImage = (CircleImageView) itemView.findViewById(R.id.img_profile);
            ServiceImage = (ImageView) itemView.findViewById(R.id.img_review);


        }


    }

    public void setListData(ArrayList<DummyReviewItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }


}


