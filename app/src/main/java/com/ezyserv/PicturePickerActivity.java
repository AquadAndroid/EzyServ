package com.ezyserv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ezyserv.custome.CustomActivity;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

/**
 * Created by Aquad on 25-07-2017.
 */

public class PicturePickerActivity extends CustomActivity {
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_picture_picker);
        imageview = (ImageView) findViewById(R.id.imageview);
        setTouchNClick(R.id.txt_gal);
        setTouchNClick(R.id.txt_cam);
        images = new ArrayList<>();
    }

    private ArrayList<Image> images;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.txt_cam) {
            ImagePicker.create(this)
                    .folderMode(false) // folder mode (false by default)
                    .folderTitle("EzyServ") // folder selection title
                    .imageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .limit(1) // max images can be selected (999 by default)
                    .showCamera(true) // show camera or not (true by default)
//                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .origin(images) // original selected images, used in multi mode
                    .start(9);
        } else if (v.getId() == R.id.txt_gal) {
            ImagePicker.create(this)
                    .folderMode(true) // folder mode (false by default)
                    .folderTitle("Folder") // folder selection title
                    .imageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .multi() // multi mode (default mode)
                    .limit(1) // max images can be selected (999 by default)
                    .showCamera(true) // show camera or not (true by default)
//                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .origin(images) // original selected images, used in multi mode
                    .start(9);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9 && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            int size = images.size();
            Bitmap myBitmap = BitmapFactory.decodeFile(images.get(0).getPath());

            imageview.setImageBitmap(myBitmap);
        }
    }
}
