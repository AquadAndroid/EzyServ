package com.ezyserv;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.utills.AppConstant;
import com.ezyserv.utills.quickblox_common.Common;
import com.ezyserv.utills.quickblox_common.FileUtils;
import com.ezyserv.utills.quickblox_common.QBChatMessageHolder;
import com.ezyserv.utills.quickblox_common.QBUserHolder;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.loopj.android.http.RequestParams;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBRoster;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.listeners.QBRosterListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.Consts;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.ui.kit.chatmessage.adapter.utils.LocationUtils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ezyserv.application.MyApp.initializeFramworkWithApp;
import static com.google.firebase.crash.FirebaseCrash.log;

public class ChatActivity extends CustomActivity implements CustomActivity.ResponseCallback {
    String TAG = ChatActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FloatingActionButton call;
    private EditText chat_box;
    TextView txtTypingStatus, txtChatToolbarTitle;
    ImageView img_profile;
    private ImageButton attach_file, send_msg;
    int IMAGE_PICK_CODE = 101;
    int VIDEO_PICK_CODE = 102;
    int LOCATION_PICK_CODE = 103;
    QBChatDialog qbChatDialog;
    MessageListAdapter chatMessageAdaprter;
    RecyclerView listViewMessages;
    Uri selectedFile;
    String filePathProfile;
    ProgressBar progressBarChatActivity;
    FullscreenVideoLayout videoLayout;
    NotificationManager mNotificationManager;
    String qbChatDialogId, chatRoomId, userIdLocal, servicemanIdLocal, fileUrl;

    QBRoster chatRoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setResponseListener(this);


        setupuiElement();

        //Cancel Notification After Accepting it from the Notification Windows
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotificationManager != null;
        mNotificationManager.cancel(1);


        setUpToolbar();

        initializeFramworkWithApp(this);

        if (!getIntent().getStringExtra("comeFrom").equals("listing")) {
            userIdLocal = getIntent().getStringExtra("user_id");
            servicemanIdLocal = getIntent().getStringExtra("serviceman_id");
            //First Call to respondService
            respondService();
        } else {
            initChatDialog();
            retrieveAllMessages();
        }

        chat_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    qbChatDialog.sendIsTypingNotification();
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    Log.e(TAG, "onTextChanged: typing " + e.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Log.e(TAG, "afterTextChanged: ");
                    qbChatDialog.sendStopTypingNotification();
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    Log.e(TAG, "onTextChanged: stop typing " + e.toString());
                }
            }
        });

        chatRoster = QBChatService.getInstance().getRoster(QBRoster.SubscriptionMode.mutual, null);
    }

    //Respond Service that has been created by the User to make status Accepted.
    void respondService() {
        RequestParams p = new RequestParams();
        p.put("createService_id", MyApp.getSharedPrefString(AppConstant.REQUESTED_SERVICE_ID));
        p.put("service_status", "accepted");
        postCall(this, AppConstant.BASE_URL + "respondService", p, "", 2);
    }

    //get the cht id from the local database
    private void getChatID(String userid, String servicemanid) {
        Log.e(TAG, "getChatID: " + userid + " : " + servicemanid);
        RequestParams p = new RequestParams();
        p.put("user_id", userid);
        p.put("serviceman_id", servicemanid);
        postCall(this, AppConstant.BASE_URL + "getChatID", p, "", 0);
    }

    //Saving the Chat Relation data (userid,servicemanid,chatroomid)
    private void saveChatRelaction(String chatRoomId) {
        RequestParams p = new RequestParams();
        p.put("serviceman_id", servicemanIdLocal);
        p.put("user_id", userIdLocal);
        p.put("chatRoomId", chatRoomId);
        postCall(this, AppConstant.BASE_URL + "saveChatRelaction", p, "", 1);
    }

    //set up of the toolbar
    void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        txtChatToolbarTitle = toolbar.findViewById(R.id.txtChatToolbarTitle);
        img_profile = toolbar.findViewById(R.id.img_profile);
        txtTypingStatus = toolbar.findViewById(R.id.txtTypingStatus);
        txtTypingStatus.setVisibility(View.INVISIBLE);
        actionBar.setTitle("");
    }

    //set up of UI elements goes here
    private void setupuiElement() {

        setTouchNClick(R.id.tv_send_address);
        setTouchNClick(R.id.img_btn_attach);
        setTouchNClick(R.id.img_btn_send_msg);
        setTouchNClick(R.id.call_btn);

        progressBarChatActivity = findViewById(R.id.progressBarChatActivity);
        listViewMessages = findViewById(R.id.listViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        listViewMessages.setLayoutManager(layoutManager);
        listViewMessages.setItemAnimator(new DefaultItemAnimator());

        //send_address = (TextView) findViewById(R.id.tv_send_address);
        chat_box = (EditText) findViewById(R.id.edt_chat_box);
        call = (FloatingActionButton) findViewById(R.id.call_btn);
        attach_file = (ImageButton) findViewById(R.id.img_btn_attach);
        send_msg = (ImageButton) findViewById(R.id.img_btn_send_msg);


    }

    //Handle on click event of control
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_send_address) {
            //startActivity(new Intent(ChatActivity.this, AddAddressActivity.class));
        } else if (v.getId() == R.id.img_btn_attach) {
            openAttachmentSelection();
        } else if (v.getId() == R.id.img_btn_send_msg) {
            if (chat_box.getText().toString().length() > 0)
                sendMessage("text", null);
            chat_box.setText("");
        } else if (v.getId() == R.id.call_btn) {
            Toast.makeText(this, "Calling.....", Toast.LENGTH_SHORT).show();
        }
    }


    void sendMessage(String type, Object attachmentObject) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        if (type.equals("text"))
            qbChatMessage.setBody(chat_box.getText().toString());
        else {
            // attach a Location
            QBAttachment attachment;
            attachment = getAttachmentLocation((String) attachmentObject);
            qbChatMessage.addAttachment(attachment);
        }
        qbChatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);

        try {
            qbChatDialog.sendMessage(qbChatMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        try {
            long lastUserActivity = QBChatService.getInstance().getLastUserActivity(qbChatDialog.getRecipientId()); //returns last activity in seconds or 0 if user online or error (e.g. user never loggedin chat)

            if (lastUserActivity > 20) {
                sendPushMessage(qbChatDialog.getRecipientId());
            }
        } catch (XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
            Log.e(TAG, "sendMessage: " + e.toString());
        }


        //Put message to catch

        QBChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), qbChatMessage);
        ArrayList<QBChatMessage> messages = QBChatMessageHolder.getInstance().getChatMessagesByDialogId(qbChatDialog.getDialogId());

        chatMessageAdaprter = new MessageListAdapter(ChatActivity.this, messages);
        listViewMessages.setAdapter(chatMessageAdaprter);
        chatMessageAdaprter.notifyDataSetChanged();

    }

    //Pop up for the attachment selection
    private void openAttachmentSelection() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attach_file);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.WRAP_CONTENT;
        lp.height = lp.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Button dialog_cancel_Button = (Button) dialog.findViewById(R.id.diag_btn_cancel);
        TextView tv_send_address = (TextView) dialog.findViewById(R.id.tv_send_address);
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
                if (isPermissionGranted(IMAGE_PICK_CODE)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, IMAGE_PICK_CODE);
                    dialog.dismiss();
                }
            }
        });
        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted(VIDEO_PICK_CODE)) {
                    Intent videoPickerIntent = new Intent();
                    videoPickerIntent.setType("video/*");
                    videoPickerIntent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(videoPickerIntent, VIDEO_PICK_CODE);
                    dialog.dismiss();
                }
            }
        });
        tv_send_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MapsActivity.class);
                intent.putExtra("from", "selection");
                startActivityForResult(intent, LOCATION_PICK_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private QBAttachment getAttachmentLocation(String location) {
        QBAttachment attachment = new QBAttachment("location");
        attachment.setData(location);
        attachment.setId(String.valueOf(location.hashCode()));

        return attachment;
    }

    //Checking runtime permission for Accessing the Local Files
    public boolean isPermissionGranted(int digit) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, digit);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    //Handling the Response taken by the Results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                selectedFile = data.getData();
                String file = FileUtils.getPath(this, selectedFile);
                Log.e(TAG, "onActivityResult: " + file);
                if (file.endsWith(".jpg") || file.endsWith(".png")) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFile);
                        filePathProfile = saveBitmapToLocal(bitmap, ChatActivity.this);
                        File sendImageFile = new File(filePathProfile);

                        openDialogImgePreview(sendImageFile, "", "fileSelected");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Invalid File Format !", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_PICK_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                selectedFile = data.getData();
                String file = FileUtils.getPath(this, selectedFile);
                Log.e(TAG, "onActivityResult: " + file);
                if (file.endsWith(".3gp") || file.endsWith(".mp4")) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Video.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String videoPath = c.getString(columnIndex);
                    c.close();
                    openVideoView(videoPath);

                } else {
                    Toast.makeText(this, "Invalid File Format !", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == LOCATION_PICK_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            double latitude = bundle.getDouble("getLatitude");
            double longitude = bundle.getDouble("getLongitude");
            String location = LocationUtils.generateLocationJson(new Pair<>("lat", latitude),
                    new Pair<>("lng", longitude));
            sendMessage("location", location);
        }
    }

    //Opening video view
    private void openVideoView(final String videoPath) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_video_picked);
        VideoView videoPreview = dialog.findViewById(R.id.videoPreview);
        ImageButton imgDialogImgSend = dialog.findViewById(R.id.imgDialogImgSend);
        videoPreview.setVideoPath(videoPath);
        videoPreview.start();
        imgDialogImgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File sendVideoFile = new File(videoPath);
                sendAttachmentImage(sendVideoFile, "vid");
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    //Saving the iamge file to local storage and deleting the same
    public String saveBitmapToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = FileUtils.getInstance(context).createTempFile("IMG_", ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
            Log.e("Tag", "Path = " + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    //Hector Call

    //Creating private chat dialog for the fist time user going to chat
    private void createChatPrivate(String qbUserId) {

        final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Please Wait. . .");
        progressDialog.setCancelable(false);
        progressDialog.show();

        QBChatDialog qbChatDialog = DialogUtils.buildPrivateDialog(Integer.parseInt(qbUserId));

        QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog dialog, Bundle bundle) {
                progressDialog.dismiss();
                qbChatDialogId = dialog.getDialogId();
                saveChatRelaction(qbChatDialogId);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: " + e.toString());
            }
        });

    }

    //Loading chat dialog from the id taken from the notification data payload
    private void loadChatDialogById(final String qbChatDialogId) {
        Log.e(TAG, "loadChatDialogById: ");
        QBRestChatService.getChatDialogById(qbChatDialogId).performAsync(
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle params) {
                        Log.e(TAG, "onSuccess: ");
                        qbChatDialog = dialog;
                        qbChatDialog.initForChat(QBChatService.getInstance());
                        initChatDialog();
                        retrieveAllMessages();
                    }

                    @Override
                    public void onError(QBResponseException responseException) {
                        Log.e(TAG, "onError: " + responseException.toString());
                    }
                });

    }

    //Initializing the chat dialog
    private void initChatDialog() {

        if (getIntent().getStringExtra("comeFrom").equals("listing")) {
            qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
            qbChatDialog.initForChat(QBChatService.getInstance());
        }

        txtChatToolbarTitle.setText(qbChatDialog.getName());

        //Register
        QBIncomingMessagesManager incomingMessages = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessages.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

                //Catch Message
                QBChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessageHolder.getInstance().getChatMessagesByDialogId(qbChatMessage.getDialogId());

                chatMessageAdaprter = new MessageListAdapter(ChatActivity.this, messages);
                listViewMessages.setAdapter(chatMessageAdaprter);
                chatMessageAdaprter.notifyDataSetChanged();

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Registering for the typing
        registerTypingForCHatDialog(qbChatDialog);

        getUserPhotoFromQBAdmin(qbChatDialog.getRecipientId());
    }

    QBChatDialogTypingListener typingListener = new QBChatDialogTypingListener() {
        @Override
        public void processUserIsTyping(String dialogId, Integer senderId) {
            if (txtTypingStatus.getVisibility() != View.VISIBLE)
                txtTypingStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void processUserStopTyping(String dialogId, Integer senderId) {
            if (txtTypingStatus.getVisibility() != View.INVISIBLE)
                txtTypingStatus.setVisibility(View.INVISIBLE);
        }
    };

    //Registering the chat dialog for the message Typing status
    private void registerTypingForCHatDialog(QBChatDialog qbChatDialog) {
        qbChatDialog.addIsTypingListener(typingListener);
    }

    //Retrieving the Old Message history
    private void retrieveAllMessages() {
        progressBarChatActivity.setVisibility(View.VISIBLE);
        final QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);

        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    //Put message to catch
                    QBChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), qbChatMessages);
                    Log.e(TAG, "onSuccess: getDialogMessages");
                    chatMessageAdaprter = new MessageListAdapter(ChatActivity.this, qbChatMessages);
                    listViewMessages.setAdapter(chatMessageAdaprter);
                    chatMessageAdaprter.notifyDataSetChanged();
                    progressBarChatActivity.setVisibility(View.GONE);
                    //getUserPhotoFromQBAdmin();
                }

                @Override
                public void onError(QBResponseException e) {
                    Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressBarChatActivity.setVisibility(View.GONE);
                }
            });
        } else {
            Log.e(TAG, "retrieveAllMessages: qbChatDialog == null");
            progressBarChatActivity.setVisibility(View.GONE);
        }

    }

    //Handle API Response in the for of Json Object
    @Override
    public void onJsonObjectResponseReceived(JSONObject o, int callNumber) {
        switch (callNumber) {
            //Result getChildId
            case 0:
                if (o.optString("status").equals("true")) {
                    try {

                        JSONObject dataJsonObject = o.getJSONObject("data");
                        createSessionForChat(MyApp.getApplication().readUser().getEmail(), "12345678");
                        chatRoomId = dataJsonObject.getString("chatRoomId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (o.optString("status").equals("false")) {
                    try {

                        JSONObject dataJsonObject = o.getJSONObject("data");
                        Log.e(TAG, "First Chat: " + dataJsonObject.toString());
                        createChatPrivate(dataJsonObject.getString("userQBid"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            //Response of saveChatRelaction API
            case 1:
                if (o.optString("status").equals("true")) {
                    try {
                        JSONObject dataJsonObject = o.getJSONObject("data");
                        Log.e(TAG, "Save Chat Relation : " + dataJsonObject.toString());

                        createSessionForChat(MyApp.getApplication().readUser().getEmail(), "12345678");
                        chatRoomId = dataJsonObject.getString("chatRoomId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                getChatID(userIdLocal, servicemanIdLocal);
                break;
        }
    }

    //Creating Session for chat to make Both valid Sender and Recipient
    private void createSessionForChat(String user, String password) {

        final QBUser qbUser = new QBUser(user, password);

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());

                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e(TAG, "onSuccess: Session Created");
                        loadChatDialogById(chatRoomId);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e(TAG, "onError: QBChatService " + e.toString());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: createSession " + e.toString());
            }
        });
    }

    //Handle API response in the format of JSONArray
    @Override
    public void onJsonArrayResponseReceived(JSONArray a, int callNumber) {

    }

    //Handle API error
    @Override
    public void onErrorReceived(String error) {

    }

    //Opening Image Preview popup Dialog
    private void openDialogImgePreview(final File sendImageFile, String uri, String from) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_picked);
        ImageView imgViewImagePicked = dialog.findViewById(R.id.imgViewImagePicked);
        ImageButton imgDialogImgSend = dialog.findViewById(R.id.imgDialogImgSend);

        if (from.equals("show")) {
            imgDialogImgSend.setVisibility(View.GONE);
            Picasso.with(this).load(uri).into(imgViewImagePicked);
        } else
            imgViewImagePicked.setImageURI(Uri.fromFile(sendImageFile));

        imgDialogImgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAttachmentImage(sendImageFile, "img");
                dialog.dismiss();
            }
        });
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    //Check The Message is Attachment or not
    private boolean hasAttachments(QBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }

    //Send Attachment Message
    private void sendAttachmentImage(File filePhoto, final String type) {

        progressBarChatActivity.setVisibility(View.VISIBLE);

        QBContent.uploadFileTask(filePhoto, true, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int i) {
                // i - progress in percentages
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile file, Bundle bundle) {
                // create a message
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setSaveToHistory(true);
                // Save a message to history

                // attach a Media
                QBAttachment attachment;
                if (type.equals("vid")) {
                    attachment = new QBAttachment(QBAttachment.VIDEO_TYPE);
                } else {
                    attachment = new QBAttachment(QBAttachment.IMAGE_TYPE);
                }


                attachment.setId(file.getId().toString());
                attachment.setUrl(file.getPublicUrl());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.addAttachment(attachment);

                // send a message
                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //Put message to catch

                QBChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessageHolder.getInstance().getChatMessagesByDialogId(qbChatDialog.getDialogId());

                chatMessageAdaprter = new MessageListAdapter(ChatActivity.this, messages);
                listViewMessages.setAdapter(chatMessageAdaprter);
                chatMessageAdaprter.notifyDataSetChanged();
                progressBarChatActivity.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                progressBarChatActivity.setVisibility(View.GONE);
            }
        });
    }

    //Retrieving the Video File
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mediaMetadataRetriever = new MediaMetadataRetriever();
            }
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    //Messages Adapter
    class MessageListAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
        private Context mContext;
        private List<QBChatMessage> mMessageList;

        public MessageListAdapter(Context context, List<QBChatMessage> messageList) {
            mContext = context;
            mMessageList = messageList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            QBChatMessage message = mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) holder).bind(message);
            }
        }

        @Override
        public int getItemViewType(int position) {


            if (mMessageList.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        private class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText;
            ImageView message_content_media, imgActionAttachment;

            SentMessageHolder(View itemView) {
                super(itemView);

                messageText = (TextView) itemView.findViewById(R.id.text_message_body);
                message_content_media = (ImageView) itemView.findViewById(R.id.message_content_media);
                imgActionAttachment = (ImageView) itemView.findViewById(R.id.imgActionAttachment);
                timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            }

            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
            void bind(QBChatMessage message) {

                if (hasAttachments(message)) {
                    Collection<QBAttachment> attachments = message.getAttachments();
                    final QBAttachment attachment = attachments.iterator().next();
                    messageText.setVisibility(View.GONE);
                    message_content_media.setVisibility(View.VISIBLE);
                    imgActionAttachment.setVisibility(View.VISIBLE);
                    if (attachment.getType().equals(QBAttachment.VIDEO_TYPE)) {
                        imgActionAttachment.setImageResource(R.drawable.play_button);
                        imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                videoPopUpView(attachment.getUrl());
                            }
                        });

                        try {
                            try {
                                Bitmap thumbnail = retriveVideoFrameFromVideo(attachment.getUrl());
                                message_content_media.setImageBitmap(thumbnail);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (attachment.getType().equals(QBAttachment.IMAGE_TYPE)) {
                        Picasso.with(mContext).load(attachment.getUrl()).into(message_content_media);
                        imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDialogImgePreview(null, attachment.getUrl(), "show");
                            }
                        });
                    } else if (attachment.getType().equals(QBAttachment.LOCATION_TYPE)) {
                        String receivedLocation = attachment.getData();
                        Pair<Double, Double> latLngPair = LocationUtils.getLatLngFromJson(receivedLocation);
                        String locationUrl = getLocationUrl(attachment, mContext);
                        Picasso.with(mContext).load(locationUrl.replaceAll("YOUR_KEY", getResources().getString(R.string.google_api_key))).into(imgActionAttachment);
                        Log.e(TAG, "bind: " + locationUrl.replaceAll("YOUR_KEY", getResources().getString(R.string.google_api_key)));
                        try {
                            JSONObject jsonObject = new JSONObject(attachment.getData());
                            Log.e(TAG, "bind: " + attachment.getData());
                            final String lat = jsonObject.getString("lat");
                            final String lng = jsonObject.getString("lng");
                            imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ChatActivity.this, MapsActivity.class);
                                    intent.putExtra("from", "SeeLocation");
                                    intent.putExtra("lat", lat);
                                    intent.putExtra("lng", lng);
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    timeText.setText(parseDateToHHMM(String.valueOf(message.getDateSent())));
                } else {
                    messageText.setText(message.getBody());
                    Log.e(TAG, "bind: " + message.getDateSent());
                    timeText.setText(parseDateToHHMM(String.valueOf(message.getDateSent())));
                }
            }
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText;
            ImageView imgActionAttachment, message_content_media_rcv;

            ReceivedMessageHolder(View itemView) {
                super(itemView);
                messageText = (TextView) itemView.findViewById(R.id.text_message_body);
                timeText = (TextView) itemView.findViewById(R.id.text_message_time);

                imgActionAttachment = (ImageView) itemView.findViewById(R.id.imgActionAttachment);
                message_content_media_rcv = (ImageView) itemView.findViewById(R.id.message_content_media_rcv);
            }

            void bind(QBChatMessage message) {


                if (hasAttachments(message)) {

                    Collection<QBAttachment> attachments = message.getAttachments();
                    final QBAttachment attachment = attachments.iterator().next();
                    messageText.setVisibility(View.GONE);
                    message_content_media_rcv.setVisibility(View.VISIBLE);
                    imgActionAttachment.setVisibility(View.VISIBLE);
                    if (attachment.getType().equals(QBAttachment.VIDEO_TYPE)) {
                        imgActionAttachment.setImageResource(R.drawable.play_button);
                        Picasso.with(mContext).load(attachment.getUrl()).into(message_content_media_rcv);
                        imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                videoPopUpView(attachment.getUrl());
                            }
                        });

                    } else if (attachment.getType().equals(QBAttachment.IMAGE_TYPE)) {
                        imgActionAttachment.setVisibility(View.VISIBLE);
                        Picasso.with(mContext).load(attachment.getUrl()).into(message_content_media_rcv);
                        imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDialogImgePreview(null, attachment.getUrl(), "show");
                            }
                        });

                    } else if (attachment.getType().equals(QBAttachment.LOCATION_TYPE)) {
                        String receivedLocation = attachment.getData();
                        Pair<Double, Double> latLngPair = LocationUtils.getLatLngFromJson(receivedLocation);
                        String locationUrl = getLocationUrl(attachment, mContext);
                        Picasso.with(mContext).load(locationUrl.replaceAll("YOUR_KEY", getResources().getString(R.string.google_api_key))).into(imgActionAttachment);
                        Log.e(TAG, "bind: " + locationUrl.replaceAll("YOUR_KEY", getResources().getString(R.string.google_api_key)));

                        imgActionAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatActivity.this, MapsActivity.class);
                                intent.putExtra("from", "SeeLocation");
                                startActivity(intent);
                            }
                        });
                    }
                    timeText.setText(parseDateToHHMM(String.valueOf(message.getDateSent())));
                } else {
                    messageText.setText(message.getBody());
                    timeText.setText(parseDateToHHMM(String.valueOf(message.getDateSent())));
                }
            }
        }
    }

    //Video Pop up view To show the Video Preview
    void videoPopUpView(String videoUri) {
        Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.dialog_video_view);
        videoLayout = dialog.findViewById(R.id.videoview);
        videoLayout.setActivity(ChatActivity.this);


        try {
            videoLayout.setVideoURI(Uri.parse(videoUri));

        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.show();
    }

    //Getting location Url to preview the Location
    public String getLocationUrl(QBAttachment attachmentLocation, Context context) {
        QBAttachment attachment = attachmentLocation;

        LocationUtils.BuilderParams params = LocationUtils.defaultUrlLocationParams(context);

        return LocationUtils.getRemoteUri(attachment.getData(), params);
    }

    //Parsing date to HH:MM formate (i.e. 03:15 pm)
    public String parseDateToHHMM(String time) {
        Log.e(TAG, "parseDateToHHMM: " + time);
        String inputPattern = "HHmmsssss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    void getUserPhotoFromQBAdmin(int opponentId) {
        QBUsers.getUser(opponentId).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                if (qbUser.getFileId() != null) {
                    int avatarId = qbUser.getFileId();
                    QBContent.getFile(avatarId).performAsync(new QBEntityCallback<QBFile>() {
                        @Override
                        public void onSuccess(QBFile qbFile, Bundle bundle) {
                            fileUrl = qbFile.getPublicUrl();
                            Picasso.with(getBaseContext()).load(fileUrl).into(img_profile);
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e(TAG, "onError: getting File " + e.toString());
                        }
                    });
                } else {
                    img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_tag_faces_black_24dp));
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: getting User By Id " + e.toString());
            }
        });
    }

    private String getUserById(int userID) {
        Log.e(TAG, "getUserById: " + userID);

        QBPresence presence = chatRoster.getPresence(userID);

        if (presence == null) {
            return "";
        }
        if (presence.getType() == QBPresence.Type.online) {
            return "online";
        } else {
            return "offline";
        }

    }

    private void sendPushMessage(Integer integer) {
        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(integer);
        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        HashMap<String, String> data = new HashMap<>();
        data.put("name", MyApp.getApplication().readUser().getName());
        data.put("message", chat_box.getText().toString());

        event.setMessage(String.valueOf(data));
        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle args) {
                Log.e(TAG, "onSuccess: Notification Sent");
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e(TAG, "onError: " + errors.toString());
            }
        });

    }


    //End Call

}
