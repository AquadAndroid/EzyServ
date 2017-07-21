package com.ezyserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.R;

import java.util.List;

/**
 * Created by DJ-PC on 6/24/2017.
 */

public class CustomAdapter extends BaseAdapter {



    private LayoutInflater layoutinflater;
    private List<ItemObject> listStorage;
    private Context context;



    public CustomAdapter(Context context, List<ItemObject> customizedListView) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }


    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.grid_item, parent, false);
            listViewHolder.CatIcon = (ImageButton) convertView.findViewById(R.id.all_cat_icon);
            listViewHolder.CatLabel = (TextView)convertView.findViewById(R.id.label);

            listViewHolder.CatIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Toast: "+position, Toast.LENGTH_SHORT).show();
                }
            });

            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.CatIcon.setImageResource(listStorage.get(position).getCatIcon());
        listViewHolder.CatLabel.setText(listStorage.get(position).getCatLabel());

        return convertView;
    }



    static class ViewHolder{
        ImageButton CatIcon;
        TextView CatLabel;

    }
}
