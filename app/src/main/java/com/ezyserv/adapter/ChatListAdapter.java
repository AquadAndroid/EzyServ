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

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.DataHolder> {
    private List<DummyChatItem> listdata;
    private LayoutInflater inflater;
    private ChatListAdapter.ItemClickCallback itemclickcallback;


    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final ChatListAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public ChatListAdapter(List<DummyChatItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public ChatListAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.chat_list_item, parent, false);
        return new ChatListAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(ChatListAdapter.DataHolder holder, int position) {
        DummyChatItem item = listdata.get(position);
        holder.img_customer.setImageResource(item.getCustomerPic());
        holder.tv_cust_name.setText(item.getCustomerName());
        holder.chat_count.setText(item.getCount());
        holder.tv_last_msg.setText(item.getLastMessage());
        holder.tv_last_date.setText(item.getLastDate());


    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }



    class DataHolder extends RecyclerView.ViewHolder {
        TextView tv_cust_name, chat_count, tv_last_msg, tv_last_date;
        ImageView img_customer;


        public DataHolder(final View itemView) {
            super(itemView);
            img_customer=(ImageView)itemView.findViewById(R.id.img_customer);
            tv_cust_name = (TextView) itemView.findViewById(R.id.tv_cust_name);
            chat_count = (TextView) itemView.findViewById(R.id.chat_count);
            tv_last_msg = (TextView) itemView.findViewById(R.id.tv_last_msg);
            tv_last_date = (TextView) itemView.findViewById(R.id.tv_last_date);




        }


    }

    public void setListData(ArrayList<DummyChatItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }




}


