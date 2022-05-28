package com.example.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Common.Constant;
import com.example.myapplication.Common.ShareApp;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MyCustomAdapter;
import com.example.myapplication.databinding.FragmentContactBinding;
import com.example.myapplication.model.ContactsInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ContactFragment extends BaseFragment implements View.OnClickListener {

    public FragmentContactBinding binding;
    public static final int RequestPermissionCode = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    MyCustomAdapter contactAdapter = null;
    List<ContactsInfo> contactsInfoList = null;
    ProgressDialog progressDialog;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, parent, false);
        binding.fetchListButton.setOnClickListener(this);
        initView();
        return binding.getRoot();
    }

    public void initView() {
        if (canAccessStorage())
            loadData();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            setContactInBackground();
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            Log.e("ContactList: ", gson.toJson(contactsInfoList));
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (contactsInfoList != null) {
                contactAdapter = new MyCustomAdapter(act, R.layout.item_common_list_layout, contactsInfoList);
                binding.contactListview.setAdapter(contactAdapter);
                contactAdapter.notifyDataSetChanged();
                saveData();
            }
            return;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage("Wait While Fetching Contacts..");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.fetchListButton.getId()) {

            if (canAccessStorage()) {
                loadData();
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    boolean isPermissionGranted = false;

    //ask for permission
    private boolean canAccessStorage() {
        // check condition
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
            isPermissionGranted = true;
            return false;
        } else {
            return true;
        }
    }

    public void setContactInBackground() {

        String contactId = null;
        String displayName = null;
        contactsInfoList = new ArrayList<>();

        Cursor cursor = act.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    ContactsInfo contactsInfo = new ContactsInfo();
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactsInfo.setContactId(contactId);
                    contactsInfo.setContactName(displayName);

                    Cursor phoneCursor = act.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);

                    if (phoneCursor.moveToNext()) {
                        @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsInfo.setPhoneNumber(phoneNumber);
                    }
                    phoneCursor.close();
                    contactsInfoList.add(contactsInfo);
                }
            }
        }
        cursor.close();
    }

    @SuppressLint("Range")
    private void getContacts() {

        String contactId = null;
        String displayName = null;
        contactsInfoList = new ArrayList<>();

        Cursor cursor = act.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    ContactsInfo contactsInfo = new ContactsInfo();
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactsInfo.setContactId(contactId);
                    contactsInfo.setContactName(displayName);

                    Cursor phoneCursor = act.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);

                    if (phoneCursor.moveToNext()) {
                        @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsInfo.setPhoneNumber(phoneNumber);
                    }
                    phoneCursor.close();
                    contactsInfoList.add(contactsInfo);
                }
            }
        }
        cursor.close();
        Gson gson = new Gson();

        Log.e("ContactList: ", gson.toJson(contactsInfoList));

        if (contactsInfoList != null) {
            contactAdapter = new MyCustomAdapter(act, R.layout.item_common_list_layout, contactsInfoList);
            binding.contactListview.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();
            saveData();
        } else {
            ShareApp.getInstance().getObserver().setValue(Constant.IS_LOADER_VISIBLE);
        }
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(act, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(act, android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                    builder.setTitle("Read contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            act.requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(act,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(act, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = act.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactsInfoList);
        editor.putString("contactList", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = act.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contactList", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<ContactsInfo>>() {
            }.getType();
            contactsInfoList = gson.fromJson(json, type);
            if (contactsInfoList == null) {
                contactsInfoList = new ArrayList<>();
                binding.fetchListButton.performClick();
            }
            contactAdapter = new MyCustomAdapter(act, R.layout.item_common_list_layout, contactsInfoList);
            binding.contactListview.setAdapter(contactAdapter);
        } else {
            new AsyncTaskRunner().execute();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (ShareApp.getInstance().getObserver().getValue() == Constant.IS_FIRST_TIME_LOAD) {

            isPermissionGranted = true;
            binding.fetchListButton.performClick();
        }
    }
}

