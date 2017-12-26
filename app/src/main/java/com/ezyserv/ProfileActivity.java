package com.ezyserv;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.model.User;
import com.ezyserv.utills.AppConstant;
import com.loopj.android.http.RequestParams;

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

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ProfileActivity extends CustomActivity implements CustomActivity.ResponseCallback {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CircleImageView img_profile;
    private ImageView img_bg;
    private TextView txt_name, txt_mail, txt_phone, txt_primary_name, txt_secondary_name;
    private User u;
    private LinearLayout ll_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResponseListener(this);
        setContentView(R.layout.activity_profile);
        u = MyApp.getApplication().readUser();
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(u.getName());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MyApp.getApplication().readUser().getName());
        toolbarTextAppearance();
        setupUiElements();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted");
                //File write logic here

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
            //do your check here
        }


        setImagePath("pic");
    }

    private void setupUiElements() {
        img_profile = findViewById(R.id.img_profile);
        img_bg = findViewById(R.id.img_bg);
        ll_services = findViewById(R.id.ll_services);



        Glide.with(getContext()).load(u.getProfilepic()).into(img_profile);
        Glide.with(getContext()).load(u.getProfilepic()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(img_bg);

        txt_name = findViewById(R.id.txt_name);
        txt_mail = findViewById(R.id.txt_mail);
        txt_phone = findViewById(R.id.txt_phone);
        txt_primary_name = findViewById(R.id.txt_primary_name);
        txt_secondary_name = findViewById(R.id.txt_secondary_name);

        txt_name.setText(u.getName());
        txt_mail.setText(u.getEmail());
        txt_phone.setText(u.getPhone());

        setTouchNClick(R.id.edt_primary);
        setTouchNClick(R.id.edt_secondary);

        try {
            txt_primary_name.setText(u.getService().getPrimary().get(0).getName());
        } catch (Exception e) {
            txt_primary_name.setText("N/A");
        }

        try {
            txt_secondary_name.setText(u.getService().getSecondary().get(0).getName());
            String names = "";
            for (int i = 0; i < u.getService().getSecondary().size(); i++) {
                names += u.getService().getSecondary().get(i).getName() + ", ";
            }
            txt_secondary_name.setText(names.substring(0, names.length() - 2));
        } catch (Exception e) {
            txt_secondary_name.setText("N/A");
        }

        if (u.getIsServicemen().equals("1")) {
            ll_services.setVisibility(View.VISIBLE);
        }

        setTouchNClick(R.id.txt_update);
        setClick(R.id.img_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        u = MyApp.getApplication().readUser();
        try {
            txt_primary_name.setText(u.getService().getPrimary().get(0).getName());
        } catch (Exception e) {
            txt_primary_name.setText("N/A");
        }

        try {
            txt_secondary_name.setText(u.getService().getSecondary().get(0).getName());
            String names = "";
            for (int i = 0; i < u.getService().getSecondary().size(); i++) {
                names += u.getService().getSecondary().get(i).getName() + ", ";
            }
            txt_secondary_name.setText(names.substring(0, names.length() - 2));
        } catch (Exception e) {
            txt_secondary_name.setText("N/A");
        }

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
        } else if (v.getId() == R.id.edt_primary) {
            startActivity(new Intent(getContext(), ChangeServicesActivity.class));
        } else if (v.getId() == R.id.edt_secondary) {
            startActivity(new Intent(getContext(), ChangeServicesActivity.class));
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
        Glide.with(this).load(imageUri).into(img_profile);
        Glide.with(getContext()).load(imageUri).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(img_bg);
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
                    Glide.with(getContext()).load(u.getProfilepic()).into(img_profile);
                    Glide.with(getContext()).load(u.getProfilepic()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(img_bg);
                }
            } else {
                MyApp.showMassage(getContext(), "Update failed.");
                Glide.with(getContext()).load(u.getProfilepic()).into(img_profile);
                Glide.with(getContext()).load(u.getProfilepic()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(img_bg);
            }
        }
    }

    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    @Override
    public void onErrorReceived(String error) {
        Glide.with(getContext()).load(u.getProfilepic()).into(img_profile);
        Glide.with(getContext()).load(u.getProfilepic()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(img_bg);
        MyApp.popMessage("Error", error, getContext());
    }
}
