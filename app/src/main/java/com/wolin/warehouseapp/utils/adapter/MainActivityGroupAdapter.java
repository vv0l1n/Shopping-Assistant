package com.wolin.warehouseapp.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivityGroupAdapter extends RecyclerView.Adapter<MainActivityGroupViewHolder>{

    private Context context;
    private MutableLiveData<List<Group>> groups;
    private ItemSelectListener itemSelectListener;

    public MainActivityGroupAdapter(Context context, MutableLiveData<List<Group>> groups, ItemSelectListener itemSelectListener) {
        this.context = context;
        this.groups = groups;
        this.itemSelectListener = itemSelectListener;
    }

    @NonNull
    @Override
    public MainActivityGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityGroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_group_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityGroupViewHolder holder, int position) {

        holder.getMainActivityGroupName().setText(groups.getValue().get(position).getName());
        holder.getGroupItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectListener.onItemClick(groups.getValue().get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
