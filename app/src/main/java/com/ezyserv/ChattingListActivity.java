package com.ezyserv;

import android.content.Context;
import android.content.Intent;
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
import com.ezyserv.utills.quickblox_common.QBUserHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hector on 12/21/17.
 */

public class ChattingListActivity extends CustomActivity {
    String TAG = ChattingListActivity.class.getSimpleName();
    Toolbar toolbar;
    RecyclerView recyclerViewUserChat;
    List<String> stringListUserChat;
    ProgressBar progressBarChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        setUpToolBar();

        stringListUserChat = new ArrayList<>();

        if (!QBChatService.getInstance().isLoggedIn())
            createSessionForChat(MyApp.getApplication().readUser().getEmail(), "12345678");

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
        progressBarChatList = findViewById(R.id.progressBarChatList);
        recyclerViewUserChat = findViewById(R.id.recyclerViewUserChat);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerViewUserChat.setLayoutManager(manager);
        recyclerViewUserChat.setHasFixedSize(true);
        recyclerViewUserChat.setItemAnimator(new DefaultItemAnimator());
    }


    private void loadChatDialogs() {
        progressBarChatList.setVisibility(View.VISIBLE);
        QBRequestGetBuilder qbRequestBuilder = new QBRequestGetBuilder();
        qbRequestBuilder.setLimit(100);


        QBRestChatService.getChatDialogs(null, qbRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                AdapterUserChat adapterUserChat = new AdapterUserChat(getBaseContext(), qbChatDialogs);
                recyclerViewUserChat.setAdapter(adapterUserChat);
                adapterUserChat.notifyDataSetChanged();
                progressBarChatList.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: loadChatDialog " + e.toString());
            }
        });

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
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.txtUserName.setText(qbChatDialogs.get(position).getName());

            holder.img_profile.setImageDrawable(getResources().getDrawable(R.drawable.markzuckerberg));

            holder.txtUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChattingListActivity.this, ChatActivity.class);
                    intent.putExtra(Common.DIALOG_EXTRA, qbChatDialogs.get(position));
                    intent.putExtra("comeFrom", "listing");
                    //intent.putExtra("chatDialogName", qbChatDialogs.get(position).getName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return qbChatDialogs.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtUserName;
            CircleImageView img_profile;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtUserName = itemView.findViewById(R.id.txtUserName);
                img_profile = itemView.findViewById(R.id.img_profile);
            }
        }
    }

    private void createSessionForChat(String user, String password) {
        Log.e(TAG, "createSessionForChat: " + user);
        //Load All uses and save to cache
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUserHolder.getInstance().putUser(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: " + e.toString());
            }
        });


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
                Log.e(TAG, "onSuccess: " + qbUser.getPassword());

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e(TAG, "onSuccess: Session Created");
                        progressBarChatList.setVisibility(View.GONE);
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

    /*@Override
    protected void onPause() {
        super.onPause();
        SignOutFromChat();
    }

    void SignOutFromChat() {
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e(TAG, "onSuccess: Sign Out");
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: " + e.toString());
            }
        });
    }*/
}
