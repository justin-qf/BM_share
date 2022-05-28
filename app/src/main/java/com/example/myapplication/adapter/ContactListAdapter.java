package com.example.myapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemContactListBinding;

import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final List<String> contactList;
    Activity activity;
    String data  ;

    public ContactListAdapter(Activity activity, List<String> tags,String contactName,String ContactNumber) {
        this.contactList = tags;
        this.activity = activity;
        data = contactName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.contactName.setText(data);
        holder.binding.phoneNumber.setText(contactList.get(1));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemContactListBinding binding;

        public ViewHolder(@NonNull ItemContactListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
