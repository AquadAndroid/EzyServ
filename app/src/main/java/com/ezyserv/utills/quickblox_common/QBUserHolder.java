package com.ezyserv.utills.quickblox_common;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector on 12/21/17.
 */

public class QBUserHolder {

    private static QBUserHolder instance;

    SparseArray<QBUser> qbUserSparseArray;

    public static synchronized QBUserHolder getInstance() {
        if (instance == null)
            instance = new QBUserHolder();
        return instance;
    }

    private QBUserHolder() {
        qbUserSparseArray = new SparseArray<>();
    }

    public void putUser(List<QBUser> users) {
        for (QBUser user : users) {
            putUser(user);
        }
    }

    public void putUser(QBUser user) {
        qbUserSparseArray.put(user.getId(), user);
    }

    public QBUser getUserById(int id) {
        return qbUserSparseArray.get(id);
    }

    public List<QBUser> getUserByIds(List<Integer> ids) {
        List<QBUser> qbUsers = new ArrayList<>();
        for (Integer id : ids) {
            QBUser user = getUserById(id);
            if (user != null)
                qbUsers.add(user);
        }
        return qbUsers;
    }

}
