package com.ezyserv;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.CustomerReviewAdapter;
import com.ezyserv.adapter.DummyReviewData;
import com.ezyserv.adapter.DummyServiceData;
import com.ezyserv.adapter.ServiceAdapter;
import com.ezyserv.custome.CustomActivity;

import java.util.ArrayList;

public class DomesticActivity extends CustomActivity {
private Toolbar toolbar;
    private ArrayList listdata;
    private RecyclerView domestic_service_recyclerView;
    private ServiceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic);
        toolbar = (Toolbar) findViewById(R.id.side_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Domestic Services");
        actionBar.setTitle("");


        domestic_service_recyclerView=(RecyclerView)findViewById(R.id.recy_domestic_service);
        listdata = (ArrayList) DummyServiceData.getListData();
        domestic_service_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(listdata, this);
        domestic_service_recyclerView.setAdapter(adapter);

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(item);
        // SearchView.SearchAutoComplete theTextArea =
        // (SearchView.SearchAutoComplete) searchView
        // .findViewById(R.id.search_src_text);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                // Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query)) {

                    Toast.makeText(DomesticActivity.this, "Enter Search Key", Toast.LENGTH_SHORT).show();
                    return false;
                }


                //performSearch(query);
                return false;
            }

        });
        // theTextArea.setTextColor(Color.WHITE);
        return true;
    }









}
