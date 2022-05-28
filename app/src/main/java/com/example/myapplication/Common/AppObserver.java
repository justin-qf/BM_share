package com.example.myapplication.Common;

import android.content.Context;

import java.util.Observable;

public class AppObserver extends Observable {
    private final Context context;
    public String emailId;
    private String userName;
    private String data;
    String DefaultName;
    private int nStatusType;

    public AppObserver(Context context) {
        this.context = context;
    }

    public int getValue() {
        return nStatusType;
    }

    public void setValue(int nStatusTyp) {
        this.nStatusType = nStatusTyp;
        setChanged();
        notifyObservers(userName);
    }
    @Override
    public boolean hasChanged() {
        return true; //super.hasChanged();
    }

    public String getData() {
        return data;
    }

    public void setValue(int nStatusTyp, String data) {
        this.nStatusType = nStatusTyp;
        this.data = data;
        setChanged();
        notifyObservers(data);
        notifyObservers();
    }

}
