package com.ezyserv.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezyserv.R;
import com.ezyserv.adapter.CustomAdapter;
import com.ezyserv.adapter.ItemObject;
import com.ezyserv.custome.CustomFragment;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryFragment extends CustomFragment {

    private TextView count;
    private ImageButton Close_categ;
    private GridView Categ_Gridview;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_all_category_fragment, container, false);
        Categ_Gridview = (GridView)view.findViewById(R.id.Cat_gridview);
        count=(TextView)view.findViewById(R.id.total_cat);
        List<ItemObject> allItems = getAllItemObject();
        CustomAdapter customAdapter = new CustomAdapter(getActivity(), allItems);
        Categ_Gridview.setAdapter(customAdapter);
        count.setText(allItems.size());
        Close_categ=(ImageButton)view.findViewById(R.id.close_cat);
        Close_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }


private List<ItemObject> getAllItemObject(){
    List<ItemObject> items = new ArrayList<>();
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));
    items.add(new ItemObject(R.drawable.all_cats,"Ac Installation"));






    return items;
}

}
