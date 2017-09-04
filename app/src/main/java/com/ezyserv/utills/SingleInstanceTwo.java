package com.ezyserv.utills;

/**
 * Created by Aditya Vats on 8/21/2017.
 */

public class SingleInstanceTwo {
    private static final SingleInstanceTwo ourInstance = new SingleInstanceTwo();

    public static SingleInstanceTwo getInstance() {
        return ourInstance;
    }

    private SingleInstanceTwo() {
    }

    private String editTextValue="0";

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;

    }
}
