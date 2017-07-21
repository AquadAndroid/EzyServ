package com.ezyserv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 7/20/2017.
 */

public class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.DataHolder> {



    private List<DummyPrmoList> listdata;
    private LayoutInflater inflater;
    private PromoCodeAdapter.ItemClickCallback itemclickcallback;
    private int count = 0;

    public interface ItemClickCallback {
        void onItemClick(int p);

        void onSecondaryIconClick(int p);

    }

    public void SetItemClickCallback(final PromoCodeAdapter.ItemClickCallback itemClickCallback) {
        this.itemclickcallback = itemClickCallback;
    }


    public PromoCodeAdapter(List<DummyPrmoList> listdata, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listdata = listdata;
    }

    @Override
    public PromoCodeAdapter.DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.promo_item, parent, false);
        return new PromoCodeAdapter.DataHolder(view);

    }

    @Override
    public void onBindViewHolder(PromoCodeAdapter.DataHolder holder, int position) {
        DummyPrmoList item = listdata.get(position);
        holder.OfferName.setText(item.getOFFERNAME());
        holder.OfferDescp.setText(item.getOFFERDISCP());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class DataHolder extends RecyclerView.ViewHolder {
        TextView OfferName, OfferDescp, Term_cond, Apply;



        public DataHolder(final View itemView) {
            super(itemView);
            OfferName = (TextView) itemView.findViewById(R.id.tv_promo_code_name);
            OfferDescp = (TextView) itemView.findViewById(R.id.tv_code_discp);
            Term_cond = (TextView)itemView.findViewById(R.id.d_tv_term_cond);

            Apply=(TextView)itemView.findViewById(R.id.btn_code_apply);

            Term_cond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Term & Condition yet to be defined", Toast.LENGTH_SHORT).show();
                }
            });

Apply.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Successfully Applied", Toast.LENGTH_SHORT).show();
    }
});
            /*Sadd_remove.setOnClickListener(new View.OnClickListener() {
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

    public void setListData(ArrayList<DummyPrmoList> exerciseList) {
        this.listdata.clear();
        this.listdata.addAll(exerciseList);

    }



}
