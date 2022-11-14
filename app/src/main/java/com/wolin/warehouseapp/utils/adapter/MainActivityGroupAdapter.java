package com.wolin.warehouseapp.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivityGroupAdapter extends RecyclerView.Adapter<MainActivityGroupViewHolder>{

    private Context context;
    private List<Group> groupList;
    private ItemSelectListener itemSelectListener;

    public MainActivityGroupAdapter(Context context, List<Group> groupList , ItemSelectListener itemSelectListener) {
        this.context = context;
        this.groupList = groupList;
        this.itemSelectListener = itemSelectListener;
    }

    @NonNull
    @Override
    public MainActivityGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityGroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_group_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityGroupViewHolder holder, int position) {
        if(groupList != null) {
            System.out.println("JESTEM TU!!!");
            Group group = groupList.get(position);
            holder.getMainActivityGroupName().setText(group.getName());
            holder.getGroupItem().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelectListener.onItemClick(group);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void updateData(List<Group> groups) {
        if(groupList != null) {
            groupList.clear();
        }
        groupList.addAll(groups);
        System.out.println("ADAPTER: " + groups);
        this.notifyItemRangeChanged(0, groups.size());
    }
}