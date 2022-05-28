package com.example.myapplication.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Common.Constant;
import com.example.myapplication.Common.ShareApp;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MyCustomAdapter;
import com.example.myapplication.databinding.FragmentRecentListBinding;
import com.example.myapplication.model.ContactsInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Observable;

public class RecentListFragment extends BaseFragment {

    public FragmentRecentListBinding binding;
    private ArrayList<ContactsInfo> callLogModelArrayList;
    private MyCustomAdapter recentCallListAdapter = null;
    public String str_number, str_contact_name;
    private static final int PERMISSIONS_REQUEST_CODE = 999;
    String[] appPermissions = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent_list, parent, false);

        initView();
        return binding.getRoot();
    }

    public void initView() {
        CheckPermission(act, appPermissions[0]);

        if (recentCallListAdapter != null) {

            if (callLogModelArrayList != null) {
                //ShareApp.getInstance().getObserver().setValue(Constant.IS_LOADER_GONE);
                binding.recentListRecyclerView.setAdapter(recentCallListAdapter);
            } else {
                //ShareApp.getInstance().getObserver().setValue(Constant.IS_LOADER_VISIBLE);
            }
        }
    }

    private void FetchCallLogs() {

        ContactsInfo callLogItem = null;
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        callLogModelArrayList = new ArrayList<>();

        String[] projection = new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE};

        Cursor managedCursor = act.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, sortOrder);

        if (managedCursor.getCount() > 0) {
            while (managedCursor.moveToNext()) {
                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                if (number > 0) {
                    callLogItem = new ContactsInfo();
                    str_number = managedCursor.getString(1);
                    str_contact_name = managedCursor.getString(0);
                    str_contact_name = str_contact_name == null || str_contact_name.equals("") ? "Unknown" : str_contact_name;

                    callLogItem.setContactName(str_contact_name);
                    callLogItem.setPhoneNumber(str_number);
                    boolean isExits = false;
                    for (ContactsInfo tmp : callLogModelArrayList) {
                        if (callLogItem.getContactName().equalsIgnoreCase(tmp.getContactName())) {
                            isExits = true;
                            break;
                        }
                    }
                    if (!isExits)
                        callLogModelArrayList.add(callLogItem);
                }
            }
        }
        managedCursor.close();
        Gson gson = new Gson();
        Log.e("callLogs", gson.toJson(callLogModelArrayList));
        recentCallListAdapter = new MyCustomAdapter(act, R.layout.item_common_list_layout, callLogModelArrayList);
        binding.recentListRecyclerView.setAdapter(recentCallListAdapter);
        recentCallListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public void CheckPermission(Context context, String Permission) {
        if (ContextCompat.checkSelfPermission(context, Permission) == PackageManager.PERMISSION_GRANTED) {
            FetchCallLogs();
        } else {
            canAccessStorage();
        }
    }
    //ask for permission
    private void canAccessStorage() {
        // check condition
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSIONS_REQUEST_CODE);
        } else {
            FetchCallLogs();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (ShareApp.getInstance().getObserver().getValue() == Constant.IS_FIRST_TIME_LOAD) {
            CheckPermission(act, appPermissions[0]);
        }
    }
}