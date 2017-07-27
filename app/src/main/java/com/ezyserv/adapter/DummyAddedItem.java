package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 5/6/2017.
 */

public class DummyAddedItem {
    private String AddedLabel;

    public String getAddedLabel() {
        return AddedLabel;
    }

    public void setAddedLabel(String addedLabel) {
        AddedLabel = addedLabel;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getVia() {
        return Via;
    }

    public void setVia(String via) {
        Via = via;
    }

    public String getAddedAmount() {
        return AddedAmount;
    }

    public void setAddedAmount(String addedAmount) {
        AddedAmount = addedAmount;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    private String BankName;
    private String Via;
    private String AddedAmount;
    private String AddedDate;


}
