package com.ezyserv;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.fragment.ChatListFragment;
import com.ezyserv.fragment.FragmentDrawer;
import com.ezyserv.fragment.GetServicesFragment;
import com.ezyserv.fragment.ServiceHomeFragment;
import com.ezyserv.fragment.ServicerequestFragment;
import com.ezyserv.utills.AppConstant;

public class ServiceHomeActivity extends CustomActivity implements FragmentDrawer.FragmentDrawerListener{
    private FragmentDrawer drawerFragment;
    private DrawerLayout drawer;
    private TextView tv_account,tv_serv_request,tv_chat;
    private ViewPager service_view_pager;
    private ImageButton serv_nav_drawer_btn;
    private TextView toolbar_name;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), null);
        drawerFragment.setDrawerListener(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.service_container, new ServiceHomeFragment()).commit();
        setupUiElements();
    }





    private void setupUiElements() {

        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_serv_request = (TextView) findViewById(R.id.tv_serv_request);
        tv_chat = (TextView) findViewById(R.id.tv_chat);

        serv_nav_drawer_btn=(ImageButton)findViewById(R.id.serv_nav_drawer_btn);
        toolbar_name=(TextView)findViewById(R.id.toolbar_name);

        setClick(R.id.rl_serv_tab_1);
        setClick(R.id.rl_serv_tab_2);
        setClick(R.id.rl_serv_tab_3);

        setClick(R.id.serv_nav_drawer_btn);

        tv_account.setSelected(true);
        tv_account.setTextColor(Color.parseColor("#ED365B"));


    }
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.rl_serv_tab_1) {

            tv_account.setSelected(true);
            tv_serv_request.setSelected(false);
            tv_chat.setSelected(false);


            tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0);
            tv_serv_request.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.menu_requests_inactive, 0, 0);
            tv_chat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats_inactive, 0, 0);



            tv_account.setTextColor(Color.parseColor("#ED365B"));
            tv_serv_request.setTextColor(Color.parseColor("#3949AB"));
            tv_chat.setTextColor(Color.parseColor("#3949AB"));

            toolbar_name.setText("My Account");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.service_container,new ServiceHomeFragment()).commit();

        } else if (v.getId() == R.id.rl_serv_tab_2) {
            tv_account.setSelected(true);
            tv_serv_request.setSelected(false);
            tv_chat.setSelected(false);


            tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_inactive, 0, 0);
            tv_serv_request.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.menu_requests_active, 0, 0);
            tv_chat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats_inactive, 0, 0);



            tv_account.setTextColor(Color.parseColor("#3949AB"));
            tv_serv_request.setTextColor(Color.parseColor("#ED365B"));
            tv_chat.setTextColor(Color.parseColor("#3949AB"));

            toolbar_name.setText("Service Requests");

            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.service_container,new ServicerequestFragment()).commit();

        } else if (v.getId() == R.id.rl_serv_tab_3) {
            tv_account.setSelected(true);
            tv_serv_request.setSelected(false);
            tv_chat.setSelected(false);


            tv_account.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_inactive, 0, 0);
            tv_serv_request.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.menu_requests_inactive, 0, 0);
            tv_chat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats_activess, 0, 0);



            tv_account.setTextColor(Color.parseColor("#3949AB"));
            tv_serv_request.setTextColor(Color.parseColor("#3949AB"));
            tv_chat.setTextColor(Color.parseColor("#ED365B"));
            toolbar_name.setText("Chats");
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.replace(R.id.service_container,new ChatListFragment()).commit();
        }else if (v.getId() == R.id.serv_nav_drawer_btn) {
            drawer.openDrawer(GravityCompat.START);

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_radius) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private Context getContext() {
        return ServiceHomeActivity.this;
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position == 0) {
            //  MyApp.showMassage(getContext(), "will go to my service requests");
            startActivity(new Intent(getContext(), ScheduleServiceActivity.class));
        } else if (position == 1) {
            MyApp.showMassage(getContext(), "will go to my service requests");
        } else if (position == 2) {
            startActivity(new Intent(getContext(), ChatActivity.class));
        } else if (position == 3) {
            MyApp.showMassage(getContext(), "will go to History");
        } else if (position == 4) {
            startActivity(new Intent(ServiceHomeActivity.this, WalletActivity.class));
        } else if (position == 5) {
            MyApp.showMassage(getContext(), "will go to promos and offers");
        } else if (position == 6) {
            MyApp.showMassage(getContext(), "will go to Favourites");
        } else if (position == 7) {

            String shareBody = "https://play.google.com/store/apps/details?" + "id=com.ezyserv";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "EzyServ (Open it in Google Play Store to Download the Application)");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            //  MyApp.showMassage(getContext(), "will invite your friends");
        } else if (position == 8) {
            MyApp.showMassage(getContext(), "will switch to service mode");
        }
    }
}
