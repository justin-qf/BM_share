package com.example.myapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Common.ShareApp;


import org.jetbrains.annotations.NotNull;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseFragment extends Fragment implements Observer {

    public ShareApp app;
    public Activity act;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        app = (ShareApp) getActivity().getApplication();
        app.getObserver().addObserver(this);
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        return provideFragmentView(inflater, parent, savedInstanseState);
    }

    public abstract View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void update(Observable observable, Object data) {

    }

}
