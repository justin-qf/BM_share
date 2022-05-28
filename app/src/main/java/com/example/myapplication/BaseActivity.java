package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Common.ShareApp;

import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static Dialog noconnectionAlertDialog;
    public Activity act;
    public static Activity staticAct;
    public ShareApp shareApp;
    private BroadcastReceiver mNetworkReceiver;
    private boolean LIVE_MODE = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticAct = this;
        act = this;
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        shareApp = (ShareApp) this.getApplication();
        shareApp.getObserver().addObserver(this);
        noconnectionAlertDialog = new Dialog(this);
        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        if (LIVE_MODE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                class getSystemService {
                }

                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        View view = act.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(innerView);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterNetworkChanges();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public BaseActivity() {
    }

    private static void showNoConnectionDialog() {
        if (!noconnectionAlertDialog.isShowing()) {
            // noconnectionAlertDialog.setContentView(R.layout.dialog_no_internet_connection);
            noconnectionAlertDialog.setCancelable(false);
            noconnectionAlertDialog.show();
        }
    }

    public static void InternetError(boolean value) {
        if (!staticAct.isDestroyed() && !staticAct.isFinishing()) {
            if (value) {
                if (noconnectionAlertDialog.isShowing()) {
                    noconnectionAlertDialog.dismiss();
                }
            } else {
                showNoConnectionDialog();
            }
        }
    }

}
