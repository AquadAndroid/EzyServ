package com.ezyserv.adapter;

/**
 * Created by DJ-PC on 3/16/2016.
 */
public class ItemObject {
    private int CatIcon;
    private String CatLabel;

    public ItemObject(int CatIcon, String CatLabel) {
        this.CatIcon = CatIcon;
        this.CatLabel = CatLabel;

    }
    public int getCatIcon() {
        return CatIcon;
    }

    public String getCatLabel() {
        return CatLabel;
    }

}
