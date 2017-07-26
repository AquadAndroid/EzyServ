package com.ezyserv.adapter;

import android.content.Context;
import android.graphics.Color;
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

/**
 * Created by HP on 7/26/2017.
 */

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.DataHolder> {
    private List<DummySavedAddressItem> listdata;
    private LayoutInflater inflater;
    private SavedAddressAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }


    public void SetItemClickCallback(final SavedAddressAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }

    public SavedAddressAdapter(List<DummySavedAddressItem> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }


    @Override
    public SavedAddressAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.saved_address_item, parent, false);
        return new SavedAddressAdapter.DataHolder(view);

    }


    @Override
    public void onBindViewHolder(SavedAddressAdapter.DataHolder holder, int position) {
        DummySavedAddressItem item = listdata.get(position);
        holder.AddressLabel.setText(item.getAddressLabel());
        holder.AddressOne.setText(item.getAddressOne());
        holder.AddressTwo.setText(item.getAddressTwo());
        holder.Pin.setText(item.getPin());
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }



    class DataHolder extends RecyclerView.ViewHolder {
        TextView AddressLabel,AddressOne, AddressTwo, Pin;
        ImageButton img_btn_delete;
        CheckBox selectAddress;


        public DataHolder(final View itemView) {
            super(itemView);
            AddressLabel = (TextView) itemView.findViewById(R.id.address_label);
            AddressOne  = (TextView) itemView.findViewById(R.id.tv_addres_one);
            AddressTwo = (TextView) itemView.findViewById(R.id.tv_address_two);
            Pin = (TextView) itemView.findViewById(R.id.tv_pin);


            img_btn_delete=(ImageButton)itemView.findViewById(R.id.img_btn_delete);
            selectAddress=(CheckBox)itemView.findViewById(R.id.chk_select_add);

           /* Sadd_remove.setOnClickListener(new View.OnClickListener() {
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

            });*/

        }


    }

    public void setListData(ArrayList<DummySavedAddressItem> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }




}


