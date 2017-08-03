package com.ezyserv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.adapter.CustomerReviewAdapter;
import com.ezyserv.adapter.DummyGallaryData;
import com.ezyserv.adapter.DummyReviewData;
import com.ezyserv.adapter.ServiceGallaryAdapter;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.utills.PicassoImageLoader;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.jeancsanchez.photoviewslider.PhotosViewSlider;

public class ServicemanProfileActivity extends CustomActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private ImageView ServiceImage;
    private TextView ServicemanName, tv_rating, availability_mode;
    private TextView serviceOffered, distance, address, additional_info;
    private RecyclerView gallery_recyclerView, reviews_recyclerView;
    private Button bookService;
    private CustomerReviewAdapter customerReviewAdapter;
    private LinearLayoutManager linearLayoutManagerHorizontal;
    private ServiceGallaryAdapter adapter;
    private ArrayList listdata;
    private ArrayList customerReviewAdapterlistdata;
    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg"
    ));
    PhotosViewSlider photoViewSlider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceman_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Serviceman Name");

//        dynamicToolbarColor();
        toolbarTextAppearance();


        setupuiElement();

    }

    private boolean isGalleryShown = false;


    private void setupuiElement() {

        setTouchNClick(R.id.btn_book_services);


        ServiceImage = (ImageView) findViewById(R.id.img_service);

        ServicemanName = (TextView) findViewById(R.id.tv_serviceman_name);
        tv_rating = (TextView) findViewById(R.id.tv_avg_rating);
        availability_mode = (TextView) findViewById(R.id.tv_availability_mode);

        serviceOffered = (TextView) findViewById(R.id.tv_service_offered);
        distance = (TextView) findViewById(R.id.tv_distance);
        address = (TextView) findViewById(R.id.tv_address);
        additional_info = (TextView) findViewById(R.id.tv_additional_info);

        bookService = (Button) findViewById(R.id.btn_book_services);

        gallery_recyclerView = (RecyclerView) findViewById(R.id.service_gallery_img);
        reviews_recyclerView = (RecyclerView) findViewById(R.id.recy_cust_review);

        linearLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listdata = (ArrayList) DummyGallaryData.getListData();
        gallery_recyclerView.setLayoutManager(linearLayoutManagerHorizontal);
        adapter = new ServiceGallaryAdapter(listdata, this);
        gallery_recyclerView.setAdapter(adapter);

        photoViewSlider = (PhotosViewSlider) findViewById(R.id.photosViewSlider);


        adapter.SetItemClickCallback(new ServiceGallaryAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int p) {
//                isGalleryShown = true;
//                photoViewSlider.initializePhotosUrls(images);
            }

            @Override
            public void onSecondaryIconClick(int p) {

            }
        });

        customerReviewAdapterlistdata = (ArrayList) DummyReviewData.getListData();

        reviews_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerReviewAdapter = new CustomerReviewAdapter(customerReviewAdapterlistdata, this);
        reviews_recyclerView.setAdapter(customerReviewAdapter);
    }

    @Override
    public void onBackPressed() {

        if (isGalleryShown) {
            isGalleryShown = false;
        } else {
            super.onBackPressed();
        }
    }

    private static final String movieUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";

    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_book_services) {
            Toast.makeText(this, "Service Booked", Toast.LENGTH_SHORT).show();

        }
    }

 /*   private void dynamicToolbarColor(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card_5);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimary)));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimaryDark)));
            }
        });
    }*/

    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

}
