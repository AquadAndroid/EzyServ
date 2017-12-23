package com.ezyserv.utills.quickblox_common;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hector on 12/21/17.
 */

public class QBChatMessageHolder {
    private static QBChatMessageHolder instance;

    HashMap<String, ArrayList<QBChatMessage>> qbChatMessageArray;

    public static synchronized QBChatMessageHolder getInstance() {
        QBChatMessageHolder qbChatMessageHolder;
        synchronized (QBChatMessageHolder.class) {
            if (instance == null)
                instance = new QBChatMessageHolder();

            qbChatMessageHolder = instance;
        }
        return qbChatMessageHolder;
    }

    private QBChatMessageHolder() {
        this.qbChatMessageArray = new HashMap<>();
    }

    public void putMessage(String dialogId, ArrayList<QBChatMessage> qbChatMessages) {
        this.qbChatMessageArray.put(dialogId, qbChatMessages);
    }

    public void putMessage(String dialogId, QBChatMessage qbChatMessages) {
        List<QBChatMessage> lstResult = this.qbChatMessageArray.get(dialogId);
        lstResult.add(qbChatMessages);
        ArrayList<QBChatMessage> lstAdded = new ArrayList<>(lstResult.size());
        lstAdded.addAll(lstResult);
        putMessage(dialogId, lstAdded);
    }

    public ArrayList<QBChatMessage> getChatMessagesByDialogId(String dialogId) {
        return this.qbChatMessageArray.get(dialogId);
    }


}
