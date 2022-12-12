package com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.ui.mainActivity.adapter.groupadapter.MainActivityGroupViewHolder;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder>{

    private Context context;
    private List<Group> groupList;
    private ItemSelectListener<Group> itemSelectListener;
    private String uid;

    public GroupAdapter(Context context, List<Group> groupList, String uid, ItemSelectListener<Group> itemSelectListener) {
        this.context = context;
        this.groupList = groupList;
        this.uid = uid;
        this.itemSelectListener = itemSelectListener;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        if(groupList != null) {
            Group group = groupList.get(position);
            holder.getGroupName().setText(group.getName());
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
        this.notifyItemRangeChanged(0, groups.size());
    }
}
