package com.ezyserv;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyserv.custome.CustomActivity;

public class ChatActivity extends CustomActivity {
    private Toolbar toolbar;
    private FloatingActionButton call;
    private TextView send_address;
    private EditText chat_box;
    private ImageButton attach_file, send_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.chat_toolbar_title);
        mTitle.setText("Company Name");
        actionBar.setTitle("");

        setupuiElement();
    }

    private void setupuiElement() {


        setTouchNClick(R.id.tv_send_address);
        setTouchNClick(R.id.img_btn_attach);
        setTouchNClick(R.id.img_btn_send_msg);
        setTouchNClick(R.id.call_btn);

        send_address = (TextView) findViewById(R.id.tv_send_address);
        chat_box = (EditText) findViewById(R.id.edt_chat_box);
        call = (FloatingActionButton) findViewById(R.id.call_btn);
        attach_file = (ImageButton) findViewById(R.id.img_btn_attach);
        send_msg = (ImageButton) findViewById(R.id.img_btn_send_msg);

    }

    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_send_address) {
            startActivity(new Intent(ChatActivity.this, AddAddressActivity.class));
        } else if (v.getId() == R.id.img_btn_attach) {
            openImage();
        } else if (v.getId() == R.id.img_btn_send_msg) {
            chat_box.setText("");
            Toast.makeText(this, "Message Send ", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.call_btn) {
            Toast.makeText(this, "Calling.....", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImage() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attach_file);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.WRAP_CONTENT;
        lp.height = lp.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.diag_btn_cancel);
        ImageButton select_pic = (ImageButton) dialog.findViewById(R.id.img_pic);
        final ImageButton select_video = (ImageButton) dialog.findViewById(R.id.img_video);
        dialog_cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "Please Select Image to Send", Toast.LENGTH_SHORT).show();
            }
        });
        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatActivity.this, "Please Select Video to send", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }
}
