package com.ezyserv.utills.quickblox_common;

import android.os.Bundle;
import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector on 12/21/17.
 */

public class QBUnreadMessageHolder {

    private static QBUnreadMessageHolder instance;

    Bundle bundle;

    public static synchronized QBUnreadMessageHolder getInstance() {
        QBUnreadMessageHolder qbUnreadMessageHolder;
        synchronized (QBChatMessageHolder.class) {
            if (instance == null)
                instance = new QBUnreadMessageHolder();
            qbUnreadMessageHolder = instance;
        }
        return qbUnreadMessageHolder;
    }


    private QBUnreadMessageHolder() {
        bundle = new Bundle();
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public int getUnreadMessageDialogById(String id) {
        return this.bundle.getInt(id);
    }
}
