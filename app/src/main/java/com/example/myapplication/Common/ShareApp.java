package com.example.myapplication.Common;

import android.annotation.SuppressLint;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;


public class ShareApp extends MultiDexApplication {

    private static ShareApp sInstance;

    private AppObserver observer;
    private RequestQueue mRequestQueue;

    public static ShareApp getsInstance() {
        return sInstance;
    }

    public synchronized static ShareApp getInstance() {
        return sInstance;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        MultiDex.install(this);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        ApplicationLifeCycle.init(sInstance);

        observer = new AppObserver(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(sInstance);

    }

    public AppObserver getObserver() {
        return observer;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
