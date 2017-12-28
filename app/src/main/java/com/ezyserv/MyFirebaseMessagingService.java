package com.ezyserv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.ezyserv.application.MyApp;
import com.ezyserv.utills.AppConstant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Aquad on 21-07-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    String TAG = "MyFirebaseMessagingService";
    public static final String MESSAGE_NOTIFICATION = "MessageNotification";
    public static final String MESAGE_ERROR = "MessageError";

    String mType, message, title, userid, providerid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        //Hector Call
        Log.e(TAG, "onMessageReceived: data: " + remoteMessage.getData());
        MyApp.setSharedPrefString(AppConstant.REQUESTED_SERVICE_ID, remoteMessage.getData().get("createService_id"));


        mType = remoteMessage.getData().get("mType");
        message = remoteMessage.getData().get("message");
        title = remoteMessage.getData().get("title");
        userid = remoteMessage.getData().get("userid");
        providerid = remoteMessage.getData().get("providerid");

        //Hector Call
        if (!TextUtils.isEmpty(mType)) {
            switch (mType) {
                case "sendRequest": // gives alert to user that request sent to provider
                    if (MyApp.getApplication().readUser().getIsServicemen().equals("0")) {
                        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
                    }
                    break;
                case "serviceAccepted":
                    sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
                    Intent registrationCompletef = null;
                    try {
                        registrationCompletef = new Intent(MESSAGE_NOTIFICATION).putExtra("type", "accepted");
                    } catch (Exception e) {
                        //If any error occurred
                        Log.e("FCMRegIntentService", "Registration error");
                        registrationCompletef = new Intent(MESAGE_ERROR);
                    }
                    //Sending the broadcast that registration is completed
                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationCompletef);
                    break;
                case "noOneAccept": // gives alert to user when no one accept the request
                    //if (MyApp.getApplication().readUser().getIsServicemen().equals("0")) {
                    sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
                    Intent registrationComplete = null;
                    try {
                        registrationComplete = new Intent(MESSAGE_NOTIFICATION).putExtra("type", "book_no_one");
                    } catch (Exception e) {
                        //If any error occurred
                        Log.e("FCMRegIntentService", "Registration error");
                        registrationComplete = new Intent(MESAGE_ERROR);
                    }
                    //Sending the broadcast that registration is completed
                    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                    //}
                    break;
                case "createService":   // this will pop up the accept reject option at provider side
                    if (MyApp.getApplication().readUser().getIsServicemen().equals("1")) {
                        SendMessageNotificationBook();
                        sendNotificationBookServiceBackGround(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));

                        MyApp.setSharedPrefString(AppConstant.SERVICES_MAN_ID, remoteMessage.getData().get("createService_id"));

                    }
                    break;
            }
        }
    }


    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    //Hector Call
    /*Sending Notification Broadcast to Main Activity */
    private void SendMessageNotificationBook() {
        Intent registrationComplete = null;
        try {
            registrationComplete = new Intent(MESSAGE_NOTIFICATION).putExtra("type", "book_serv");
        } catch (Exception e) {
            //If any error occurred
            Log.e("FCMRegIntentService", "Registration error");
            registrationComplete = new Intent(MESAGE_ERROR);
        }
        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    //Hector Call
    /*Sending Notification If App is In back ground to Accept Reject Service Request*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendNotificationBookServiceBackGround(String message, String title) {


        Intent acceptIntent = new Intent(this, ChatActivity.class);
        acceptIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        acceptIntent.putExtra("comeFrom", "Notif");
        acceptIntent.putExtra("user_id", userid);
        acceptIntent.putExtra("serviceman_id", providerid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, acceptIntent, PendingIntent.FLAG_ONE_SHOT);


        Intent denyIntent = new Intent(this, AutoDismissReceiver.class);
        denyIntent.setAction("NO_ACTION");
        denyIntent.putExtra("notificationId", 1);
        PendingIntent denyPendingIntent = PendingIntent.getBroadcast(this, 0, denyIntent, 0);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new Notification.Builder(this)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_check_black_24dp, "Accept", pendingIntent) // #0
                .addAction(R.drawable.ic_close_black_24dp, "Deny", denyPendingIntent)  // #1
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1 /* ID of notification */, notification);
    }

    //Hector Call to dismiss the notification
    public static class AutoDismissReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notificationId = intent.getIntExtra("notificationId", 1);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.cancel(notificationId);
        }
    }


}
