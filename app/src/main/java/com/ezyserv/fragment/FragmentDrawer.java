package com.ezyserv.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezyserv.R;
import com.ezyserv.SignUpSelection;
import com.ezyserv.adapter.NavigationDrawerAdapter;
import com.ezyserv.application.MyApp;
import com.ezyserv.utills.AppConstant;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentDrawer extends Fragment {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private TextView profile_name;
    private TextView txt_logout;
    private TextView nav_item_switch_profile;
    private TextView nav_item_scheduled;
    //private TextView nav_item_customer_support;
    private TextView nav_item_invite_friends;
    private TextView nav_item_promo_offer;
    private TextView nav_item_fav;
    private TextView nav_item_service_request;
    private RelativeLayout rl_profile;
    private RelativeLayout nav_item_chats;
    private RelativeLayout nav_item_notification;
    private RelativeLayout nav_item_wallet;
    private LinearLayout ll_menu;
    private CircleImageView img_profile;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<String> getData() {
        List<String> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            data.add(titles[i]);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(
                R.array.nav_drawer_labels);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        //  recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        profile_name = (TextView) layout.findViewById(R.id.profile_name);
        txt_logout = (TextView) layout.findViewById(R.id.txt_logout);
        profile_name.setText(MyApp.getApplication().readUser().getName());
        nav_item_scheduled = (TextView) layout.findViewById(R.id.nav_item_scheduled);
        nav_item_invite_friends = (TextView) layout.findViewById(R.id.nav_item_invite_friends);
        nav_item_fav = (TextView) layout.findViewById(R.id.nav_item_fav);
        nav_item_switch_profile = (TextView) layout.findViewById(R.id.nav_item_switch_profile);
        nav_item_promo_offer = (TextView) layout.findViewById(R.id.nav_item_promo_offer);
        nav_item_service_request = (TextView) layout.findViewById(R.id.nav_item_service_request);
        ll_menu = (LinearLayout) layout.findViewById(R.id.ll_menu);
        nav_item_chats = (RelativeLayout) layout.findViewById(R.id.nav_item_chats);
        rl_profile = (RelativeLayout) layout.findViewById(R.id.rl_profile);
        nav_item_notification = (RelativeLayout) layout.findViewById(R.id.nav_item_notification);
        nav_item_wallet = (RelativeLayout) layout.findViewById(R.id.nav_item_wallet);

        img_profile = (CircleImageView) layout.findViewById(R.id.img_profile);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img_profile.getLayoutParams();
        if (Build.VERSION.SDK_INT >= 21) {
            lp.setMargins(0, getStatusBarHeight() + 5, 0, 0);
        } else {
            lp.setMargins(0, getStatusBarHeight(), 0, 0);
        }

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });
        ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        nav_item_scheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 0);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_service_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 1);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 2);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        nav_item_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 3);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 4);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_promo_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 5);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_switch_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 8);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        nav_item_invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 7);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        nav_item_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(v, 6);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.setStatus(AppConstant.IS_LOGIN, false);
                startActivity(new Intent(getActivity(), SignUpSelection.class));
                getActivity().finishAffinity();
            }
        });


        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout,
                      final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements
            RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(
                                    e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(child,
                                        recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null
                    && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}