package com.ezyserv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezyserv.application.MyApp;
import com.ezyserv.custome.CustomActivity;
import com.ezyserv.utills.quickblox_common.Common;
import com.ezyserv.utills.quickblox_common.QBUnreadMessageHolder;
import com.ezyserv.utills.quickblox_common.QBUserHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hector on 12/21/17.
 */

public class ChattingListActivity extends CustomActivity implements QBChatDialogMessageListener {
    String TAG = ChattingListActivity.class.getSimpleName();
    Toolbar toolbar;
    RecyclerView recyclerViewUserChat;
    List<String> stringListUserChat;
    //ProgressBar progressBarChatList;
    ProgressDialog progressDialog;
    String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        setUpToolBar();

        stringListUserChat = new ArrayList<>();

        if (!QBChatService.getInstance().isLoggedIn())
            createSessionForChat(MyApp.getApplication().readUser().getEmail(), "12345678");
        else
            loadChatDialogs();

    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.side_toolbar_chat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView mTitle = toolbar.findViewById(R.id.side_toolbar_title);
        mTitle.setText("Chat");
        actionBar.setTitle("");
        setupuiElement();
    }

    private void setupuiElement() {
        //progressBarChatList = findViewById(R.id.progressBarChatList);
        recyclerViewUserChat = findViewById(R.id.recyclerViewUserChat);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerViewUserChat.setLayoutManager(manager);
        recyclerViewUserChat.setHasFixedSize(true);
        recyclerViewUserChat.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while fetching your chat History");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    private void loadChatDialogs() {
        QBRequestGetBuilder qbRequestBuilder = new QBRequestGetBuilder();
        qbRequestBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null, qbRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {

                final AdapterUserChat adapterUserChat = new AdapterUserChat(getBaseContext(), qbChatDialogs);
                Log.e(TAG, "onSuccess: Dialog Count : " + qbChatDialogs);
                Set<String> setIds = new HashSet<>();
                for (QBChatDialog chatDialog : qbChatDialogs) {
                    setIds.add(chatDialog.getDialogId());
                }

                QBRestChatService.getTotalUnreadMessagesCount(setIds, QBUnreadMessageHolder.getInstance().getBundle())
                        .performAsync(new QBEntityCallback<Integer>() {
                            @Override
                            public void onSuccess(Integer integer, Bundle bundle) {
                                QBUnreadMessageHolder.getInstance().setBundle(bundle);
                                recyclerViewUserChat.setAdapter(adapterUserChat);
                                adapterUserChat.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                Log.e(TAG, "onError: " + e.toString());
                            }
                        });

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: loadChatDialog " + e.toString());
            }
        });

    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        loadChatDialogs();
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

    }


    // Adapter for listing the Providers with whom User had done chat already
    class AdapterUserChat extends RecyclerView.Adapter<AdapterUserChat.MyViewHolder> {

        Context context;
        ArrayList<QBChatDialog> qbChatDialogs;

        public AdapterUserChat(Context baseContext, ArrayList<QBChatDialog> qbChatDialogs) {
            this.context = baseContext;
            this.qbChatDialogs = qbChatDialogs;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adpater_chat_user_list, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txtUserName.setText(qbChatDialogs.get(position).getName());

            int unread_count = QBUnreadMessageHolder.getInstance().getBundle().getInt(qbChatDialogs.get(position).getDialogId());
            if (unread_count > 0) {
                holder.txtUnreadMessageCount.setText(String.valueOf(unread_count));
            } else {
                holder.txtUnreadMessageCount.setVisibility(View.GONE);
            }


            QBUsers.getUser(qbChatDialogs.get(position).getRecipientId()).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    if (qbUser.getFileId() != null) {
                        int avatarId = qbUser.getFileId();
                        QBContent.getFile(avatarId).performAsync(new QBEntityCallback<QBFile>() {
                            @Override
                            public void onSuccess(QBFile qbFile, Bundle bundle) {
                                fileUrl = qbFile.getPublicUrl();
                                Picasso.with(context).load(fileUrl).into(holder.img_profile);
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                Log.e(TAG, "onError: getting File " + e.toString());
                            }
                        });
                    } else {
                        holder.img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_tag_faces_black_24dp));
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(TAG, "onError: getting User By Id " + e.toString());
                }
            });

            holder.txtUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChattingListActivity.this, ChatActivity.class);
                    intent.putExtra(Common.DIALOG_EXTRA, qbChatDialogs.get(position));
                    intent.putExtra("comeFrom", "listing");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return qbChatDialogs.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtUserName, txtUnreadMessageCount;
            CircleImageView img_profile;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtUserName = itemView.findViewById(R.id.txtUserName);
                img_profile = itemView.findViewById(R.id.img_profile);
                txtUnreadMessageCount = itemView.findViewById(R.id.txtUnreadMessageCount);
            }
        }
    }

    private void createSessionForChat(String user, String password) {
        //Load All uses and save to cache
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUserHolder.getInstance().putUser(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
            }
        });


        progressDialog.show();
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
                        QBIncomingMessagesManager qbIncomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
                        qbIncomingMessagesManager.addDialogMessageListener(ChattingListActivity.this);
                        loadChatDialogs();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e(TAG, "onError: QBChatService " + e.toString());
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: createSession " + e.toString());
                progressDialog.dismiss();
            }

        });
    }

}
