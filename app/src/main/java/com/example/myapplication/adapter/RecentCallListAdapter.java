package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.Common.Constant;
import com.example.myapplication.Common.ShareApp;
import com.example.myapplication.Listener.ListClickListener;
import com.example.myapplication.R;
import com.example.myapplication.model.CallLogModel;
import com.example.myapplication.model.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

public class RecentCallListAdapter extends ArrayAdapter {

    private List callLogModelArrayList;
    private Context context;

    public RecentCallListAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.callLogModelArrayList = objects;
        this.context = context;

    }
    private class ViewHolder {
        TextView displayName;
        TextView phoneNumber;
        ConstraintLayout main_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_common_list_layout, null);

            holder = new ViewHolder();
            holder.displayName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
            holder.main_layout = (ConstraintLayout) convertView.findViewById(R.id.main_contact_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        CallLogModel currentLog = (CallLogModel) callLogModelArrayList.get(position);
        holder.displayName.setText(currentLog.getContactName());
        holder.phoneNumber.setText(currentLog.getPhNumber());

        ViewHolder finalHolder = holder;
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualString = currentLog.getPhNumber();

                if (actualString.contains("+91")) {
                    Log.e("yes", "+91");
                    actualString = actualString.replace("+91", "").toString();
                    actualString = actualString.replaceAll("[\\D]", "");

                } else if (actualString.contains("91")) {
                    Log.e("yes", "+91");
                    actualString = actualString.replace("91", "").toString();
                    actualString = actualString.replaceAll("[\\D]", "");
                } else if (actualString.contains("+91 ")) {
                    actualString = actualString.replace("91", "").toString();
                    actualString = actualString.replaceAll("[\\D]", "");
                } else {
                    actualString = actualString.replaceAll("[\\D]", "");
                }
                ShareApp.getInstance().getObserver().setValue(Constant.PIC_FROM_CONTACT_LIST, actualString);
            }
        });

        return convertView;
    }
}
