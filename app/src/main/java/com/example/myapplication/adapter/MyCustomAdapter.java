package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.Common.Constant;
import com.example.myapplication.Common.ShareApp;
import com.example.myapplication.R;
import com.example.myapplication.model.ContactsInfo;

import java.util.List;

public class MyCustomAdapter extends ArrayAdapter {

    private List contactsInfoList;
    private Context context;
    Animation scaleUp;

    public MyCustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.contactsInfoList = objects;
        this.context = context;
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.top_animation);
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
        // holder.main_layout.startAnimation(scaleUp);
        ContactsInfo contactsInfo = (ContactsInfo) contactsInfoList.get(position);
        holder.displayName.setText(contactsInfo.getContactName());
        holder.phoneNumber.setText(contactsInfo.getPhoneNumber());
        holder.main_layout.setOnClickListener(v -> {
            String actualString = contactsInfo.getPhoneNumber();
            if (actualString.trim().startsWith("+91")) {
                actualString = actualString.replace("+91", "91");
                actualString = actualString.replaceAll("[\\D]", "");
            } else if (actualString.startsWith("91")) {
                //actualString = actualString.replace("91", "91");
                actualString = actualString.replaceAll("[\\D]", "");
            } else if (actualString.startsWith("+91 ")) {
                actualString = actualString.replace("91", "91");
                actualString = actualString.replaceAll("[\\D]", "");
            } else if (actualString.startsWith("+")) {
                actualString = actualString.replaceAll("[\\D]", "");
                Log.e("actualString", actualString);
            } else {
                actualString = actualString.replaceAll("[\\D]", "");
            }
            ShareApp.getInstance().getObserver().setValue(Constant.PIC_FROM_CONTACT_LIST, actualString);
        });
        return convertView;
    }

}
