package com.ezyserv.utills.quickblox_common;

import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by Hector on 12/21/17.
 */


public class Common {

    public static final String DIALOG_EXTRA = "Dialogs";

    public static String createChatDialogName(List<Integer> qbUsers) {
        List<QBUser> qbUsers1 = QBUserHolder.getInstance().getUserByIds(qbUsers);
        StringBuilder name = new StringBuilder();
        for (QBUser user : qbUsers1)
            name.append(user.getFullName()).append(" ");

        if (name.length() > 30) {
            name = name.replace(30, name.length() - 1, "...");
        }
        return String.valueOf(name);
    }



}
