package com.ezyserv.application;

/**
 * Created by Aquad on 21-07-2017.
 */

public class SingleInstance {
    private static final SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }

    private SingleInstance() {
    }
}
