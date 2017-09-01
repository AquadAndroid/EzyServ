package com.ezyserv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.fragment.AddedFragment;
import com.ezyserv.fragment.PaidFragment;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.ezyserv.utills.BlurTransformation;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CircleImageView img_profile;
    private ImageView img_bg;
    private TextView txt_name, txt_mail, txt_phone;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_profile);
        u = MyApp.getApplication().readUser();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(u.getName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MyApp.getApplication().readUser().getName());
        toolbarTextAppearance();
        setupUiElements();
        setImagePath("pic");
    }

    private void setupUiElements() {
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        img_bg = (ImageView) findViewById(R.id.img_bg);

        Picasso.with(getContext()).load(u.getProfilepic()).into(img_profile);
        Picasso.with(getContext()).load(u.getProfilepic()).transform(new BlurTransformation(getContext())).into(img_bg);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_mail = (TextView) findViewById(R.id.txt_mail);
        txt_phone = (TextView) findViewById(R.id.txt_phone);

        txt_name.setText(u.getName());
        txt_mail.setText(u.getEmail());
        txt_phone.setText(u.getPhone());

        setTouchNClick(R.id.txt_update);
        setClick(R.id.img_profile);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.txt_update) {

        } else if (v == img_profile) {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Choose Image").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    d.dismiss();
                    takePicture();
                }
            }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    d.dismiss();
                    openGallery();
                }
            }).create().show();
        }
    }

    private Context getContext() {
        return ProfileActivity.this;
    }

    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    //    =========================================   Image setup  =================================

    private File mFileTemp;
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CROP_ICON = 0x3;
    private String fileString = "";

    private void setImagePath(String name) {
        File sdIconStorageDir = new File(
                Environment.getExternalStorageDirectory()
                        + "/EzyServ/Pictures/");
        sdIconStorageDir.mkdirs();
        mFileTemp = new File(Environment.getExternalStorageDirectory()
                + "/EzyServ/Pictures/", name + ".jpg");

    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            if (mFileTemp != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        mFileTemp);
//                Uri photoURI = Uri.parse(mFileTemp.getAbsolutePath().toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
            }
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
            /*
             * The case if image selected from device gallery
			 */
                case REQUEST_CODE_GALLERY:

                    try {
                        InputStream inputStream = getContentResolver()
                                .openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                mFileTemp);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        cropImage(rotateImage(getContext(), mFileTemp));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

			/*
             * The case if image is captured from camera
			 */
                case REQUEST_CODE_TAKE_PICTURE:

                    cropImage(rotateImage(getContext(), mFileTemp));
                    break;

                case REQUEST_CROP_ICON:
                    if (data != null) {
                        Bitmap photo = data.getExtras().getParcelable("data");

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        try {
                            mFileTemp.createNewFile();
                            FileOutputStream fo = new FileOutputStream(mFileTemp);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;

            }
        }
    }

    private void cropImage(File mFileTemp) {
        long length = mFileTemp.length() / 1024; // Size in KB

//        if (length > 600) {
//            mFileTemp = saveBitmapToFile(mFileTemp);
//
//        }
        fileString = mFileTemp.getAbsolutePath();
        File file = new File(fileString);
        Uri imageUri = Uri.fromFile(file);

        callChangeProfilePicture(fileString);
        Picasso.with(this).load(imageUri).memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().resize(150, 150).into(img_profile);
        Picasso.with(getContext()).load(imageUri).transform(new BlurTransformation(getContext())).into(img_bg);
    }

    private void callChangeProfilePicture(String path) {
        RequestParams p = new RequestParams();
        p.put("user_id", u.getId());
        String contentType = RequestParams.APPLICATION_OCTET_STREAM;
        try {
            p.put("profilepic", mFileTemp, contentType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        postCall(getContext(), AppConstant.BASE_URL + "changeProfilePic", p, "Uploading...", 1);
    }


    public static File rotateImage(Context context, final File path) {
        Bitmap b = decodeFileFromPath(context, path.getAbsolutePath());
        try {
            ExifInterface ei = new ExifInterface(path.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Log.d("Orientation", "Orientation value : " + orientation);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    break;
                default:
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        FileOutputStream out1 = null;
//        File file;
        try {
//            String state = Environment.getExternalStorageState();
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".jpg");
//                Log.d("PictureUtils","uses environmment media mounted : "+Environment.getExternalStorageDirectory());
//                Log.d("PicturesUtils","file path :"+file.getPath());
//                Log.d("PicturesUtils","file name :"+file.getName());
//            }
//            else {
//                file = new File(context.getFilesDir() , "image" + new Date().getTime() + ".jpg");
//                Log.d("PictureUtils","uses getFilesDire : "+context.getFilesDir());
//            }
            out1 = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out1);
//            imgRotatedPath = file.getAbsolutePath();
            //imgFromCameraOrGallery.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out1.close();
            } catch (Throwable ignore) {

            }
        }
        return path;
    }


    public static Bitmap decodeFileFromPath(Context context, String path) {
        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            int inSampleSize = 1024;
            if (o.outHeight > inSampleSize || o.outWidth > inSampleSize) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(inSampleSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = context.getContentResolver().openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            return b;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 4;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        if (callNumber == 1) {
            if (o.optString("status").equals("true")) {
                MyApp.showMassage(getContext(), o.optString("Message"));
                try {
                    u.setProfilepic(o.getJSONObject("data").optString("profilepic"));
                    MyApp.getApplication().writeUser(u);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Picasso.with(getContext()).load(u.getProfilepic()).into(img_profile);
                    Picasso.with(getContext()).load(u.getProfilepic()).transform(new BlurTransformation(getContext())).into(img_bg);
                }
            } else {
                MyApp.showMassage(getContext(), "Update failed.");
                Picasso.with(getContext()).load(u.getProfilepic()).into(img_profile);
                Picasso.with(getContext()).load(u.getProfilepic()).transform(new BlurTransformation(getContext())).into(img_bg);
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        Picasso.with(getContext()).load(u.getProfilepic()).into(img_profile);
        Picasso.with(getContext()).load(u.getProfilepic()).transform(new BlurTransformation(getContext())).into(img_bg);
        MyApp.popMessage("Error", error, getContext());
    }
}
