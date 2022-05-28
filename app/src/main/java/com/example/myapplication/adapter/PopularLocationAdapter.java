package com.example.myapplication.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Listener.ListClickListener;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemRecentListLayoutBinding;
import com.example.myapplication.model.CallLogModel;
import java.util.ArrayList;

public class PopularLocationAdapter extends RecyclerView.Adapter<PopularLocationAdapter.ViewHolder> {
    private Activity activity;
    private ListClickListener listClickListener;
    ArrayList<CallLogModel> callLogModelArrayList;

    public PopularLocationAdapter(Activity activity, ArrayList<CallLogModel> callLogModelArrayList) {
        this.callLogModelArrayList = callLogModelArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recent_list_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CallLogModel currentLog = callLogModelArrayList.get(position);
        holder.binding.phoneNumber.setText("asbckasjbncndkncd");
        holder.binding.contactName.setText(currentLog.getContactName());

        Log.e("tv_ph_num", holder.binding.phoneNumber.getText().toString());
        Log.e("tv_contact_name", holder.binding.contactName.getText().toString());


//        holder.binding.rlPopularLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listClickListener.onClickListener(view, position, frameItems.get(position));
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return callLogModelArrayList.size();
    }

    public void setListener(ListClickListener viewVendor) {
        this.listClickListener = viewVendor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecentListLayoutBinding binding;

        public ViewHolder(@NonNull ItemRecentListLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
